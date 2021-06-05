package com.example.service;

import java.util.List;

import com.example.bean.Report;

public interface SaveReport {

    public Report genReport();

    public  boolean save(Report report);

    public  Report get(String id);

    public List<Report> getAll();

    public boolean appendMsg(String id, String msg);
}
