package com.taobao.monitor.common.ao.center;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.db.impl.center.KeyDao;
import com.taobao.monitor.common.po.CspAppKeyRelation;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.po.CspKeyPropertyRelation;
import com.taobao.monitor.common.po.CspNeedBaseline;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.util.page.Pagination;

public class KeyAo {
	private static KeyAo keyAo = new KeyAo();
	
	private KeyAo(){};
	
	private KeyDao keyDao = new KeyDao();
	
	public static KeyAo get(){
		return keyAo;
	}
	
	public Pagination<CspKeyMode> findAlarmKeyListByAppIdPageable(int appId,
			String keyNamePart, int pageNo, int pageSize) {
		return keyDao.findAlarmKeyListByAppIdPageable(appId, keyNamePart, pageNo, pageSize);
	}
	
	public boolean updateKeyMode(CspKeyMode po){
		return keyDao.updateKeyMode(po);
	}
	public boolean addKeyMode(CspKeyMode po) {
		return keyDao.addKeyMode(po);
	}
	/**
	 * ��ѯ�����е�key
	 * @return
	 */
	public List<KeyPo> findAllKeyInfo(){		
		List<KeyPo> list = new ArrayList<KeyPo>();
		list.addAll(keyDao.findAllKey().values());
		return list;
	}
	
	
	public KeyPo addMonitorKey(String keyName){
		return keyDao.addMonitorKey(keyName);
	}
	
	public KeyPo getKeyByName(String keyName){
		return keyDao.getKeyPo(keyName);
	}
	
	public KeyPo getKeyByName(int keyId){
		return keyDao.getKeyPo(keyId);
	}

	/**
	 * ��ѯ������key
	 * 
	 * @return
	 */
	public Map<Integer, KeyPo> findAllKey() {
		return keyDao.findAllKey();
	}
	
	/**
	 * ȡ������Ӧ�õ�key
	 * 
	 * @param appId
	 * @return
	 */
	public List<KeyPo> findAllAppKey(int appId) {

		return keyDao.findAllAppKey(appId);

	}
	
	/**
	 * ���� key ���� ģ����ѯ��������ص� key ��Ϣ���������б�
	 * 
	 * @param name
	 * @return
	 */
	public List<KeyPo> findKeyLikeName(String keyname, Integer appId){
		return keyDao.findKeyLikeName(keyname, appId);
	}
	
	/**
	 * ����key info
	 * @param po
	 * @return
	 */
	public boolean updateKeyInfo(KeyPo po){
		return keyDao.updateKeyInfo(po);
	}
	
	/**
	 * ��ѯ��Ҫ��ʵʱ���baseline���������ݵ�key
	 * 
	 * @return
	 */
	public List<KeyPo> findAllMonitorBaseLineKey(int appId) {
		return keyDao.findAllMonitorBaseLineKey(appId);
	}

	
	
	
	
	/////////////////////////////////////////////////////////////////////////////
	public List<CspAppKeyRelation> findAllAppKeyRelation(){
		return keyDao.findAllAppKeyRelation();
	}
	public boolean findKeyByKeyId(Integer keyId,final CspKeyInfo cki){
		return keyDao.findKeyByKeyId(keyId, cki);
	}
	public List<CspKeyPropertyRelation> findAllKeyPropertyRelation(){
		return keyDao.findAllKeyPropertyRelation();
	}
	public List<CspKeyPropertyRelation> findKeyPropertyRelation(Integer keyId){
		return keyDao.findKeyPropertyRelation(keyId);
	}
	public List<CspKeyPropertyRelation> findKeyPropertyRelation(String keyName){
		return keyDao.findKeyPropertyRelation(keyName);
	}
	public void addAppKeyRelation(int appId,int keyId){
		keyDao.addAppKeyRelation(appId, keyId);
	}
	
	public void addKeyPropertyRelation(CspKeyPropertyRelation kp){
		 keyDao.addKeyPropertyRelation(kp);
	}
	
	public CspKeyInfo  addKeyInfo(CspKeyInfo info){
		return keyDao.addKeyInfo(info);
	}
	
	
	public CspKeyInfo getKeyInfo(String keyName){
		return keyDao.getKeyInfo(keyName);
	}
	
	
	public List<CspKeyInfo> findAllKeyInfos(){
		return keyDao.findAllKeyInfos();
	}
	
	
	public List<CspKeyMode> findAllKeyModes(){
		return keyDao.findAllKeyModes();
	}
	
	public CspKeyMode getKeyMode(String appName,String keyname,String propertyname,String scope){
		return keyDao.getKeyMode(appName, keyname, propertyname, scope);
	}
	

	public CspKeyMode getKeyMode(String appName,String keyname,String propertyname){
		return keyDao.getKeyMode(appName, keyname, propertyname);
	}
	

	public CspKeyMode getKeyMode(int id){
		return keyDao.getKeyMode(id);
	}
	
	/**
	 * ���� key ���� ģ����ѯ��������ص� key ��Ϣ���������б�
	 * 
	 * @param name
	 * @return
	 */
	public List<CspKeyInfo> findKeyLikeName(String keyname) {
		return keyDao.findKeyLikeName(keyname);
		
	}
	
	public List<CspKeyInfo> findCspKeyLikeName(String keyname,Integer appId,Integer n){
		return keyDao.findCspKeyLikeName(keyname, appId, n);
	}
	
	public List<CspKeyInfo> findKeyListByAppId(int appId){
		return keyDao.findKeyListByAppId(appId);
	}
	
	
	/**
	 * ͨ��key�����ƻ�ȡ���key ��������������б�
	 * @param keyName
	 * @return
	 */
	public List<String> findKeyPropertyNames(String keyName){
		return keyDao.findKeyPropertyNames(keyName);
	}
	
	
	public Pagination<CspKeyInfo> findKeyListByAppIdPageable(int appId, String keyNamePart,  int pageNo, int pageSize){
		return keyDao.findKeyListByAppIdPageable(appId, keyNamePart, pageNo, pageSize);
	}
	public List<CspKeyInfo> findChild(String parentKey) {
		return keyDao.findChild(parentKey);
	}

	
	public List<CspKeyMode> findKeyModes(String appName, String keyName) {
		return keyDao.findKeyModes(appName, keyName);
	}
	
	public List<CspKeyMode> findKeyModes(String appName) {
		return keyDao.findKeyModes(appName);
	}
	
	public List<CspKeyMode> findKeyModes(String appName, String keyName,String property){
		return keyDao.findKeyModes(appName, keyName, property);
	}
	public List<CspKeyMode> getKeyModeByAppName(String appName){
		return keyDao.getKeyModeByAppName(appName);
	}
	
	/**
	 * ��ѯĳ��Ӧ���µ����key����������key
	 * @param appId
	 * @param parentKey
	 * @return
	 */
	public List<CspKeyInfo> findKeyChildByApp(int appId,String parentKey){
		return keyDao.findKeyChildByApp(appId,parentKey);
	}

	
	
	public boolean deleteKeyMode(int id){
		return keyDao.deleteKeyMode(id);
		
	}
	
	
	/**
	 * ��ȡȫ��hsf ��ǵ�key ��Ӧ�ö�Ӧ��Ϣ
	 *@author xiaodu
	 * @return
	 * @throws Exception
	 *TODO
	 */
	public Map<String,String> findAppNameByHsfKeyName()  {
		return keyDao.findAppNameByHsfKeyName();
	}
	
	/**
	 * 
	 *@author xiaodu
	 * @param need
	 *TODO
	 */
	public void addNeedBaseline(CspNeedBaseline need){
		keyDao.addNeedBaseline(need);
	}
	
	/**
	 * 
	 *@author xiaodu
	 *TODO
	 */
	public void deleteAllNeedBaseline(){
		keyDao.deleteAllNeedBaseline();
	}
	
	public Map<String,List<CspNeedBaseline>> findAllNeedBaseline(){
		return keyDao.findAllNeedBaseline();
	}
}
