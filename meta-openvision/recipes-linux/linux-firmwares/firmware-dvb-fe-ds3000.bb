require linux-firmware.inc

DESCRIPTION = "Firmware for ds3000 dvb frontend"

do_install() {
	install -d ${D}${nonarch_base_libdir}/firmware
	install -m 0644 dvb-fe-ds3000.fw ${D}${nonarch_base_libdir}/firmware
}
