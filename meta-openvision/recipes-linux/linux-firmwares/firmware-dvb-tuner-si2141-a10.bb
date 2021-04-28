require linux-firmware.inc

DESCRIPTION = "Firmware for tuner si2141 a10-01"

do_install() {
	install -d ${D}${nonarch_base_libdir}/firmware
	install -m 0644 dvb-tuner-si2141-a10-01.fw ${D}${nonarch_base_libdir}/firmware
}
