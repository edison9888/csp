package com.taobao.csp.depend.po.tddl;

public class TablePo {
  private String tableName;
  private String dbName;
  private String dbDispName;
  private String ip;
  private int port;
  private ColumnPo[] columns;
  private IndexPo[] indexes;
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getDbName() {
    return dbName;
  }
  public void setDbName(String dbName) {
    this.dbName = dbName;
  }
  public String getDbDispName() {
    return dbDispName;
  }
  public void setDbDispName(String dbDispName) {
    this.dbDispName = dbDispName;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public int getPort() {
    return port;
  }
  public void setPort(int port) {
    this.port = port;
  }
  public ColumnPo[] getColumns() {
    return columns;
  }
  public void setColumns(ColumnPo[] columns) {
    this.columns = columns;
  }
  public IndexPo[] getIndexes() {
    return indexes;
  }
  public void setIndexes(IndexPo[] indexes) {
    this.indexes = indexes;
  }
}
