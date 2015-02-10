package com.nyx.maven;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

public class TestActiviti {

	private ProcessEngine processEngine = null;
	
	@Before
	public void before(){
		//自动加载classpath下面的activiti.cfg.xml
		processEngine = ProcessEngines.getDefaultProcessEngine();
	}

	@Test
	public void getProcessEngine() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(processEngine);
	}

	/**
	 * 使用activiti相关API步骤
	 * 1.创建核心流程引擎对象ProcessEngine
	 * 2.使用ProcessEngine获取需要的服务对象
	 * 3.使用服务对象相关方法完成操作
	 */
	//1.发布流程规则
	@Test
	public void deployFlow(){

		// 1.1	 使用ProcessEngine获取需要的服务对象
		RepositoryService repositoryService = processEngine.getRepositoryService();
		
		//1.2	使用服务对象相关方法完成操作(创建发布对象)
		DeploymentBuilder builder = repositoryService.createDeployment();
		
		//1.3	指定发布相关的文件
		builder.addClasspathResource("HelloWorld.bpmn").addClasspathResource("HelloWorld.png");
		
		//1.4	发布流程
		
		builder.deploy();
		
		//1.5	查询相关表有没有数据
		//	act_re_procdef、act_ge_bytearray、act_re_deployment
		
	}
	
	//2.启动流程实例
	@Test
	public void startFlow() throws Exception {
		
		//2.1	使用ProcessEngine获取运行服务时对象
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		//2.1	启动流程
		runtimeService.startProcessInstanceByKey("HelloWorld");
	}
	
	//3.查询任务
	@Test
	public void queryTask() throws Exception {
		//3.1	使用ProcessEngine获取任务服务对象
		TaskService taskService = processEngine.getTaskService();
		
		//3.2	创建查询对象
//		String userId = "张三";
		String userId = "李四";
		//		添加过滤条件	查询任务
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
		
		for(Task task:tasks){
			System.out.println("ID:"+task.getId());
			System.out.println("Name:"+task.getName());
			System.out.println("Assignee:"+task.getAssignee());
			System.out.println("CreateTime:"+task.getCreateTime());
		}
		
	}
	
	//4.办理任务
	@Test
	public void completeTask() throws Exception {
		//4.1	使用ProcessEngine获取任务服务对象
		TaskService taskService = processEngine.getTaskService();
		
		//4.2	使用任务服务完成任务
//		String taskId = "5004";
		String taskId = "7502";
		taskService.complete(taskId);
	}
}
