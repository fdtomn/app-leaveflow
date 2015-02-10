package com.nyx.maven;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author fdtomn
 *
 */
public class TestProcessDefinition {

	private ProcessEngine processEngine = null;
	
	private RepositoryService repositoryService = null;
	
	@Before
	public void before() {
		//获取流程引擎对象
		processEngine = ProcessEngines.getDefaultProcessEngine();
		//获取仓库服务对象
		repositoryService = processEngine.getRepositoryService();
	}
	
	
	/**
	 * 1.发布流程规则
	 * 		发布成功后,会在数据库中的3张表添加数据
	 * 		act_ge_bytearray	添加2条数据（规则文件和流程图片）
	 * 		act_re_deployment	添加1条数据（定义了新规则的显示名称和发布时间）
	 * 		act_re_procdef		添加1条数据（流程规则相关信息[流程定义]）
	 * 	
	 * 		ID = {key}:{version}:{随机数}
	 * 
	 * 		zip方式发布流程规则,流程发布成功后 Activiti会自动解压zip包
	 * @throws Exception
	 */
	@Test
	public void deployProcess() throws Exception {
		//1.创建一个发布配置对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//2.添加发布的资源文件(流程规则文件和流程图片)
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("LeaveFlow.bpmn");
		InputStream inputStreamPNG = Thread.currentThread().getContextClassLoader().getResourceAsStream("LeaveFlow.png");
		deploymentBuilder
			.name("请假流程")
			.addInputStream("LeaveFlow.bpmn", inputStream)
			.addInputStream("LeaveFlow.png", inputStreamPNG);
		
		deploymentBuilder.deploy();
	}
	
	@Test
	public void deployProcessByZip() throws Exception {
		//1.创建一个发布配置对象
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		
		//2.添加发布的资源文件
		//从当前类所在包下加载资源
//		this.getClass().getResourceAsStream("LeaveFlow.zip")
		//从classpath根目录加载指定的资源文件
		InputStream inputStream = this.getClass().getResourceAsStream("/LeaveFlow.zip");
//		this.getClass().getClassLoader().getResourceAsStream("LeaveFlow.zip"); //也是可以的
//		Thread.currentThread().getContextClassLoader().getResourceAsStream("LeaveFlow.bpmn"); //classpath
		
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		deploymentBuilder.addZipInputStream(zipInputStream);
		deploymentBuilder.deploy();
		
	}
	
}
