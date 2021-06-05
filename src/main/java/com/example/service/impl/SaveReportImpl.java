package com.example.service.impl;

import java.util.List;
import java.util.UUID;

import com.example.bean.Report;
import com.example.service.SaveReport;
import com.example.util.ECache;
import com.example.util.LineNotify;

import org.springframework.stereotype.Service;

@Service
public class SaveReportImpl implements SaveReport{

    private LineNotify lineNotify = new LineNotify();

    public String genUUID(){
        return UUID.randomUUID().toString();
    }

    public Report genReport(){
        Report report = new Report();
        report.setId(genUUID());
        return report;
    }

    @Override
    public synchronized boolean save(Report report) {
        ECache.put(report.getId(), report);
        if (get(report.getId()) != null ){
            lineNotify.callEvent("ASnId3qHAhspft9uQpVQk8rClIjCfU9V8iNdDBUEASU", report.getTitle() + "\n" + report.getContent());
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean appendMsg(String id, String msg) {
        Report report = ECache.get(id);
        if(report == null){
            return false;
        }
        report.setContent(report.getContent() + "\n" + msg);
        ECache.put(id, report);

        Report savedReport = get(id);
        if(savedReport.getContent().indexOf(msg) >= 0){
            lineNotify.callEvent("ASnId3qHAhspft9uQpVQk8rClIjCfU9V8iNdDBUEASU", report.getTitle() + "\n" + report.getContent());
            return true;
        }
        return false;
    }


    @Override
    public Report get(String id) {
        return ECache.get(id);
    }

    @Override
    public List<Report> getAll() {
        return ECache.getList();
    }
    
}
