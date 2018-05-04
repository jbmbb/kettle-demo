package com.xxlai.kettledemo.test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.util.CollectionUtils;

public class KettleTest {

	public static void main(String[] args) {
//		String fileName = "D:/eclipse.4.4.2/workspace/kettle-demo/src/main/resources/properties/task1.ktr";
//		runTransfer(null, fileName);
		
		String fileName = "D:/eclipse.4.4.2/workspace/kettle-demo/src/main/resources/properties/job1.kjb";
		runJob(null, fileName);
		
		System.out.println("OK");
		System.exit(0);
	}

	/**
	 * 通过文件方式执行转换
	 * 
	 * @param params
	 * @param ktrPath
	 */
	public static void runTransfer(String[] params, String ktrPath) {
		try {
			// 初始化
			KettleEnvironment.init();

			TransMeta transMeta = new TransMeta(ktrPath);
			// 转换
			Trans trans = new Trans(transMeta);

			// 执行转换
			trans.execute(params);
			// 等待转换执行结束
			trans.waitUntilFinished();

			if (trans.getErrors() > 0) {
				throw new Exception(
						"There are errors during transformation exception!(传输过程中发生异常)");
			}
		} catch (KettleException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过文件方式执行job
	 * 
	 * @param maps
	 * @param jobPath
	 */
	public static void runJob(Map<String, String> maps, String jobPath) {
		try {
			KettleEnvironment.init();
			// jobname 是job脚本的路径及名称
			JobMeta jobMeta = new JobMeta(jobPath, null);
			Job job = new Job(null, jobMeta);

			// 向job脚本传递参数，脚本中获取参数值：${参数名}
			// job.setVariable(paraname, paraValue);
			if(!CollectionUtils.isEmpty(maps)){
				Set<Entry<String, String>> set = maps.entrySet();
				for (Iterator<Entry<String, String>> it = set.iterator(); it
						.hasNext();) {
					Entry<String, String> entry = it.next();
					job.setVariable(entry.getKey(), entry.getValue());
				}
			}
			job.start();
			job.waitUntilFinished();
			if (job.getErrors() > 0) {
				throw new Exception(
						"There are errors during job exception!(执行job发生异常)");
			}
		} catch (KettleException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 待测试
	 * 执行资源库中的转换
	 * 
	 * @throws KettleException
	 */
	public static void runWithDb() throws KettleException {
		KettleEnvironment.init();
		//创建DB资源库
		KettleDatabaseRepository repository = new KettleDatabaseRepository();
		DatabaseMeta databaseMeta = new DatabaseMeta("kettle", "mysql", "jdbc",
				"localhost", "staff", "3306", "xxlai", "123456");
		//选择资源库
		KettleDatabaseRepositoryMeta repositoryMeta = new KettleDatabaseRepositoryMeta("kettle", "kettle", "Transformation description", databaseMeta);
		repository.init(repositoryMeta);
		//连接资源库
		repository.connect("xxlai", "123456");
		
		RepositoryDirectoryInterface directoryInterface = repository.loadRepositoryDirectoryTree();
		TransMeta transMeta = repository.loadTransformation("demo1", directoryInterface,null,true,null);
		Trans trans = new Trans(transMeta);
		trans.execute(null);
		trans.waitUntilFinished();
		if(trans.getErrors()>0){
			System.out.println("transformation error");
		}else{
			System.out.println("transformation successfully");
		}
	}
}
