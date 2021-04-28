inherit image_types

IMAGE_TYPEDEP_amikoemmc = "ext4"

do_image_amikoemmc[depends] = " \
    parted-native:do_populate_sysroot \
    dosfstools-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    virtual/kernel:do_populate_sysroot \
    amiko-buildimage-native:do_populate_sysroot \
    "

IMAGE_CMD_amikoemmc () {
    mkdir -p ${IMGDEPLOYDIR}/userdata
    mkdir -p ${IMGDEPLOYDIR}/userdata/linuxrootfs1
    mkdir -p ${IMGDEPLOYDIR}/userdata/linuxrootfs2
    mkdir -p ${IMGDEPLOYDIR}/userdata/linuxrootfs3
    mkdir -p ${IMGDEPLOYDIR}/userdata/linuxrootfs4
    cp -fR --preserve=mode,links ${IMAGE_ROOTFS}/* ${IMGDEPLOYDIR}/userdata/linuxrootfs1/
    eval local COUNT=\"0\"
    eval local MIN_COUNT=\"60\"
    if [ $ROOTFS_SIZE -lt $MIN_COUNT ]; then
        eval COUNT=\"$MIN_COUNT\"
    fi
    dd if=/dev/zero of=${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext4 seek=${IMAGE_ROOTFS_SIZE} count=$COUNT bs=1024
    mkfs.ext4 -F -i 4096 ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext4 -d ${IMGDEPLOYDIR}/userdata
}