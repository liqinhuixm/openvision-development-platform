MODULE = "SnmpAgent"
DESCRIPTION = "SNMP Agent"

RDEPENDS_${PN} = "enigma2-plugin-extensions-bitrate twistedsnmp"

require conf/license/license-gplv2.inc
require openplugins-replace-vision.inc
require openplugins-distutils.inc
