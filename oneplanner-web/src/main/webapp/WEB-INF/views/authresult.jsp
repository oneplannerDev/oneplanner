<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script type="text/javascript">
  $(function() {
    $('#myModal').modal('show');
    $('#myModal').on('hidden.bs.modal', function () {
      window.location.href="https://play.google.com/store/apps/details?id=com.ontide.oneplanner";
    });
  });
  </script>
</head>
<body>
<div class="container">
  <div class="modal fade in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
  data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="myModalLabel">${authResultTitle}</h4>
        </div>
        <div class="modal-body">
        ${authResultMessage}
        </div>
        <div class="modal-footer">
          <form method="post">
            <button type="button" class="btn btn-default" data-dismiss="modal">${authResultButton}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
