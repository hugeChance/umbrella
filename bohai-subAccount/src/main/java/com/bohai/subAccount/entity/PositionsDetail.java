package com.bohai.subAccount.entity;

public class PositionsDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_POSITIONS_DETAIL.SUBUSERID
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    private String subuserid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_POSITIONS_DETAIL.COMBOKEY
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    private String combokey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_POSITIONS_DETAIL.INSTRUMENTID
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    private String instrumentid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_POSITIONS_DETAIL.DIRECTION
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    private String direction;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_POSITIONS_DETAIL.VOLUME
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    private Short volume;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_POSITIONS_DETAIL.SUBUSERID
     *
     * @return the value of T_POSITIONS_DETAIL.SUBUSERID
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public String getSubuserid() {
        return subuserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_POSITIONS_DETAIL.SUBUSERID
     *
     * @param subuserid the value for T_POSITIONS_DETAIL.SUBUSERID
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public void setSubuserid(String subuserid) {
        this.subuserid = subuserid == null ? null : subuserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_POSITIONS_DETAIL.COMBOKEY
     *
     * @return the value of T_POSITIONS_DETAIL.COMBOKEY
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public String getCombokey() {
        return combokey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_POSITIONS_DETAIL.COMBOKEY
     *
     * @param combokey the value for T_POSITIONS_DETAIL.COMBOKEY
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public void setCombokey(String combokey) {
        this.combokey = combokey == null ? null : combokey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_POSITIONS_DETAIL.INSTRUMENTID
     *
     * @return the value of T_POSITIONS_DETAIL.INSTRUMENTID
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public String getInstrumentid() {
        return instrumentid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_POSITIONS_DETAIL.INSTRUMENTID
     *
     * @param instrumentid the value for T_POSITIONS_DETAIL.INSTRUMENTID
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public void setInstrumentid(String instrumentid) {
        this.instrumentid = instrumentid == null ? null : instrumentid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_POSITIONS_DETAIL.DIRECTION
     *
     * @return the value of T_POSITIONS_DETAIL.DIRECTION
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public String getDirection() {
        return direction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_POSITIONS_DETAIL.DIRECTION
     *
     * @param direction the value for T_POSITIONS_DETAIL.DIRECTION
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_POSITIONS_DETAIL.VOLUME
     *
     * @return the value of T_POSITIONS_DETAIL.VOLUME
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public Short getVolume() {
        return volume;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_POSITIONS_DETAIL.VOLUME
     *
     * @param volume the value for T_POSITIONS_DETAIL.VOLUME
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    public void setVolume(Short volume) {
        this.volume = volume;
    }
}