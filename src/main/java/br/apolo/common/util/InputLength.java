package br.apolo.common.util;

public class InputLength {
	public static final int ERROR_MESSAGE = 4000;

	public static final int DB_OBJECT_NAME = 128;

	public static final int NATURAL_PRIMARY_KEY = 60;

	public static final int URL = 256;

	public static final int FLAG = 1;

	public static final int ISO_2_LETTER = 2;

	public static final int ISO_3_LETTER = 3;

	public static final int INTERNATIONAL_PHONE_CODE = 4;

	public static final int IP_ADDRESS = 15;

	public static final int TYPE = 10;

	public static final int KEY = 50;

	public static final int SHORT_KEY = 25;

	public static final int LONG_KEY = 100;

	public static final int VALUE = 150;

	public static final int SHORT_VALUE = 50;

	public static final int LONG_VALUE = 300;

	public static final int NAME = 80;

	public static final int SHORT_NAME = 60;

	public static final int LONG_NAME = 100;

	public static final int VERY_LONG_NAME = 200;

	public static final int DESCR = 500;

	public static final int SHORT_DESCR = 100;

	public static final int LONG_DESCR = 1000;

	public static final int SHORT_STATUS = 1;

	public static final int LONG_STATUS = 20;

	public static final int STATUS = 10;

	public static final int CODE = 10;

	public static final int SHORT_CODE = 5;

	public static final int LONG_CODE = 20;

	public static final int LARGE = 1024;

	public static final int MEDIUM = 256;

	public static final int SMALL = 64;

	public static final int TINY = 16;

	public static final int ESTIMATED_TIME_NBR = 8;

	/* These getters are necessary for the EL expressions inside the JSPages. They must be non-static in order to work */

	public int getErrorMessage() {
		return ERROR_MESSAGE;
	}

	public int getDbObjectName() {
		return DB_OBJECT_NAME;
	}

	public int getNaturalPrimaryKey() {
		return NATURAL_PRIMARY_KEY;
	}

	public int getUrl() {
		return URL;
	}

	public int getFlag() {
		return FLAG;
	}

	public int getIso2Letter() {
		return ISO_2_LETTER;
	}

	public int getIso3Letter() {
		return ISO_3_LETTER;
	}

	public int getInternationalPhoneCode() {
		return INTERNATIONAL_PHONE_CODE;
	}

	public int getIpAddress() {
		return IP_ADDRESS;
	}

	public int getType() {
		return TYPE;
	}

	public int getKey() {
		return KEY;
	}

	public int getShortKey() {
		return SHORT_KEY;
	}

	public int getLongKey() {
		return LONG_KEY;
	}

	public int getValue() {
		return VALUE;
	}

	public int getShortValue() {
		return SHORT_VALUE;
	}

	public int getLongValue() {
		return LONG_VALUE;
	}

	public int getName() {
		return NAME;
	}

	public int getShortName() {
		return SHORT_NAME;
	}

	public int getLongName() {
		return LONG_NAME;
	}

	public int getVeryLongName() {
		return VERY_LONG_NAME;
	}

	public int getDescr() {
		return DESCR;
	}

	public int getShortDescr() {
		return SHORT_DESCR;
	}

	public int getLongDescr() {
		return LONG_DESCR;
	}

	public int getShortStatus() {
		return SHORT_STATUS;
	}

	public int getLongStatus() {
		return LONG_STATUS;
	}

	public int getStatus() {
		return STATUS;
	}

	public int getCode() {
		return CODE;
	}

	public int getShortCode() {
		return SHORT_CODE;
	}

	public int getLongCode() {
		return LONG_CODE;
	}

	public int getLarge() {
		return LARGE;
	}

	public int getMedium() {
		return MEDIUM;
	}

	public int getSmall() {
		return SMALL;
	}

	public int getTiny() {
		return TINY;
	}

	public int getEstimatedTimeNbr() {
		return ESTIMATED_TIME_NBR;
	}

}
