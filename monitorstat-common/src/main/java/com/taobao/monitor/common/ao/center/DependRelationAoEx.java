package com.taobao.monitor.common.ao.center;


/**
 * Relation����ʹ�õ��߼�
 * @author zhongting.zy
 *
 */
public class DependRelationAoEx {
  private final static DependRelationAoEx dependAo = new DependRelationAoEx();
  private DependRelationAoEx() {
  }
  
  public static DependRelationAoEx get() {
    return dependAo;
  }
  
  
  
  public static void main(String[] args) {
  }
}
