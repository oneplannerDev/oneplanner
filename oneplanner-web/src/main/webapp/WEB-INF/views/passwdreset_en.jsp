<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>One planner Password reset</title> 
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <style>
  body {font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;font-size: 14px;line-height: 1.42857143;color: #333;background-color: #fff;}
.panel {margin-top: 5px;margin-bottom: 20px;background-color: #fff;border: 1px solid transparent;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0,0,0,.05);box-shadow: 0 1px 1px rgba(0,0,0,.05);}
.panel-info {border-color: #bce8f1;}
.panel-heading {color: #31708f;background-color: #d9edf7;border-color: #bce8f1;padding: 10px 15px;border-bottom: 1px solid transparent;border-top-left-radius: 3px;border-top-right-radius: 3px;}
.panel-body {padding: 15px;}
  </style>
</head>
<body>
<div class="container">
  <div class="panel panel-info">
    <div class="panel-heading">One planner Password reset</div>
    <div class="panel-body">Dear ${userName},<br>
      <br>
      Thank you for using One planner service.<br>
      <br>
      You have recently requested for a reset of password. Here is your temporary password.<br>
      <span style="color: #31708f;background-color: #d9edf7;">${passwd}</span><br>
      <br>
      You will be prompted to update to a password of your choice once you have logged in.</div>
  </div>
</div>
</body>
</html> 
