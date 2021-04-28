SUMMARY = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-${PV}/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "^(gb800seplus|gb800ueplus|gbipbox|gbquad|gbquadplus|gbultrase|gbultraue|gbultraueh|gbx1|gbx2|gbx3|gbx3h)$"

inherit kernel machine_kernel_pr samba_change_dialect

KERNEL_RELEASE = "${KERNELVERSION}"

SRC_URI[md5sum] = "39e7cdac18f40870e5f3de0e94bbac1f"
SRC_URI[sha256sum] = "db6dbddb0f3fe757c1bcc4685b56f3aaf83c7c07ecd9e4c6a994226406b82a0d"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-base"
PKG_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_PACKAGE_NAME}-image"
RPROVIDES_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-${KERNEL_VERSION}"
RPROVIDES_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_PACKAGE_NAME}-image-${KERNEL_VERSION}"

SRC_URI = "http://source.mynonpublic.com/gigablue/linux/gigablue-linux-${PV}-mips-20170302.tgz \
	file://defconfig \
	file://${OPENVISION_BASE}/meta-openvision/recipes-linux/kernel-patches/kernel-add-support-for-gcc${VISIONGCCVERSION}.patch \
	file://0001-genet1-1000mbit.patch \
	file://bcmgenet_phyaddr.patch \
	file://noforce_correct_pointer_usage.patch \
	file://0001-Support-TBS-USB-drivers-for-4.6-kernel.patch \
	file://0001-TBS-fixes-for-4.6-kernel.patch \
	file://0001-STV-Add-PLS-support.patch \
	file://0001-STV-Add-SNR-Signal-report-parameters.patch \
	file://blindscan2.patch \
	file://0001-stv090x-optimized-TS-sync-control.patch \
	file://0002-log2-give-up-on-gcc-constant-optimizations.patch \
	file://0003-cp1emu-do-not-use-bools-for-arithmetic.patch \
	file://0002-makefile-disable-warnings.patch \
	"

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_OUTPUT = "vmlinux"
KERNEL_IMAGETYPE = "vmlinux"
KERNEL_IMAGEDEST = "tmp"

FILES_${KERNEL_PACKAGE_NAME}-image = "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz"


kernel_do_install_append() {
    ${STRIP} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    gzip -9c ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} > ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
    rm -f ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
}

pkg_postinst_${KERNEL_PACKAGE_NAME}-image () {
    if [ "x$D" == "x" ]; then
        if [ -f ${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz ] ; then
            flash_erase /dev/${MTD_KERNEL} 0 0
            nandwrite -p /dev/${MTD_KERNEL} ${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
            rm -f ${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
        fi
    fi
    true
}

do_rm_work() {
}

# extra tasks
addtask kernel_link_images after do_compile before do_install