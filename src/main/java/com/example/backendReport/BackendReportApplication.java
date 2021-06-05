package com.example.backendReport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.bean.Report;
import com.example.bean.response.Response;
import com.example.bean.response.impl.NormalResponse;
import com.example.service.SaveReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
@ComponentScan(value = "com.example.service.*")
@CrossOrigin( origins = "https://localhost:8080", maxAge = 3600)
@RequestMapping("/api")
public class BackendReportApplication {

	@Autowired
	SaveReport saveReport;

	private static final Logger logger 
		= LoggerFactory.getLogger(BackendReportApplication.class);

	// private ECache eCache;
	public static void main(String[] args) {
		SpringApplication.run(BackendReportApplication.class, args);
	}

	@GetMapping("/helloworld")
	public String hello() {
		return "Hello World!";
	}

	/** */
	// @PostMapping("/content")
	// public Response addContent(@RequestParam("id") String contentId,@RequestParam String title ,@RequestParam String msg){
	// 	NormalResponse a = new NormalResponse();

	// 	Report report = new Report();
	// 	report.setId(contentId);
	// 	report.setTitle(title);
	// 	report.setContent(msg);

	// 	a.setSuccess(saveReport.save(report));

	// 	a.setMsg(a.isSuccess()? "存檔成功" : "存檔失敗");
	// 	return a;
	// }

	/**
	 * 取得 ALL Report
	 * @return
	 */
	@GetMapping("/report")
	public List<Report> getAllReprot(){
		return saveReport.getAll().stream().sorted((o1,o2)->o2.getCreatetime().compareTo(o1.getCreatetime())).collect(Collectors.toList());
	}

	/**
	 * 取得 1 個 Report(Content)
	 * @param contentId
	 * @return
	 */
	@GetMapping("/content")
	public Report getContent(@RequestParam("id") String contentId){
		logger.info(saveReport.get(contentId).getContent());
		return saveReport.get(contentId);
	}

	/**
	 * append Msg 1 個 Report(Content)
	 * @param contentId
	 * @param msg
	 * @return
	 */
	@PutMapping("/content")
	public Response editContent(@RequestBody Report requestReport){//(@RequestParam("id") String contentId, @RequestParam String msg){
		NormalResponse a = new NormalResponse();
		String contentId = requestReport.getId();
		String content = requestReport.getContent();
		if(!"".equals(requestReport.getId())){
			a.setSuccess(saveReport.appendMsg(contentId, content));
		}else{
			a.setSuccess(false);
		}

		a.setMsg(a.isSuccess()? "存檔成功" : "存檔失敗");
		return a;
	}

	/**
	 * 新增一個 Report
	 * @param title
	 * @param content
	 * @return
	 */
	@PostMapping("/report")
	public Response addReport(@RequestBody Report requestReport){//(@RequestParam String title,@RequestParam String content){
		NormalResponse a = new NormalResponse();

		Report report = saveReport.genReport();
		// report.setId(contentId);
		String title = requestReport.getTitle();
		String content = requestReport.getContent();

		if(!"".equals(title)){
			report.setTitle(title);
			report.setContent(content);
			report.setCreatetime(new Date());
			a.setSuccess(saveReport.save(report));
		}else{
			a.setSuccess(false);
		}


		a.setMsg(a.isSuccess()? "存檔成功" : "存檔失敗");
		return a;
	}


	public Response editReport(@RequestParam String id,@RequestParam String title,@RequestParam String content){

		NormalResponse a = new NormalResponse();
		Report report = saveReport.get(id);
		// report.setId(contentId);
		report.setTitle(title);
		report.setContent(content);

		a.setSuccess(saveReport.save(report));

		a.setMsg(a.isSuccess()? "存檔成功" : "存檔失敗");
		return a;
	}
	// @Scheduled(cron = "0 0 8 ? * MON,TUE,WED,THU,FRI *")
	@Scheduled(cron = "0 0 8 * * MON,TUE,WED,THU,FRI")
	public void everygoup(){
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd");
		String strDate = dateFormat.format(date);
		createReport(strDate + "上班回報");
	}

	@Scheduled(cron = "0 0 17 * * MON,TUE,WED,THU,FRI")
	public void everygodown(){
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd");
		String strDate = dateFormat.format(date);
		createReport(strDate + "下班回報");
	}

	@Scheduled(cron = "0 0 15 * * MON,TUE,WED,THU,FRI")
	public void everygocc(){
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd");
		String strDate = dateFormat.format(date);
		createReport(strDate + "防疫回報");
	}

	private void createReport(String title){
		logger.info("建立新的 Report" + title);
		Report report = saveReport.genReport();
		String content = "";
		report.setTitle(title);
		report.setContent(content);
		report.setCreatetime(new Date());
		saveReport.save(report);
		// ECache.put("testDate", "test_write::"+  new Date());
		// logger.info("test::" + ECache.get("testDate"));
	}

	// @Scheduled(cron = "* * * * * SUN,MON,TUE,WED,THU,FRI")
	public void showTime(){
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String strDate = dateFormat.format(date);
		logger.info("現在時間:"+ strDate);
	}


}
