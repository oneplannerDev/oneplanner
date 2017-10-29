app.component('adminLogin', {
  templateUrl : "views/login.html",
  controller : function($rootScope,$scope, $state, $stateParams, $filter, UtilsService, StatusService,RESOURCES) {
    $scope.admin = {};
    $scope.admin.userId = "admin";
    $scope.admin.passwd = "";
    //$rootScope.loginInfo = {};
    $scope.loginAdmin = function() {
      StatusService.loginAdmin($scope.admin).then(function(resp) {
        if (resp.data.resultCode == "0001") { //success
          console.log("$rootScope.globals:"+$filter('json')($rootScope.globals));
          $rootScope.$watch('globals', function() {
            console.log("watch: "+$filter('json')($rootScope.globals));
            if ($rootScope.globals.currentUser == undefined) {
              console.log("watch: log out ");
              $state.go('adminLogin');
            }
          });
          $state.go('intro',{from:''});
        } else if (resp.data.resultCode == "4005") { //invalid passwd
          console.log("login error no passwd");
          UtilsService.msgboxAlert("패스워드가 입력한 값과 다릅니다.");
          UtilsService.focusElement("pwd");
        } else if (resp.data.resultCode == "4001") {//user not found
          console.log("login error no userId");
          UtilsService.msgboxAlert("아이디를 찾을수 없습니다.");
          UtilsService.focusElement("usr");
        } else {
          console.log("login error");
          UtilsService.msgboxAlert("로그인중 오류가 발생했습니다.");
        }
      });
    };

  }
});


app.component('adminUpdate', {
  templateUrl : "views/admin/admin_update.html",
  controller : function($scope, $state, $rootScope, $filter, UtilsService, UsersService,StatusService,RESOURCES) {
    console.log("adminUpdate");
    $scope.adminForUpdate = {};
    console.log("$rootScope.globals="+$filter('json')($rootScope.globals));
    currentUser = $rootScope.globals.currentUser;

    $scope.adminForUpdate.userId = currentUser.userId;
    $scope.adminForUpdate.passwd = currentUser.passwd;
    $scope.adminForUpdate.email = currentUser.email;
    $scope.isEmailSubmitValid = true;
    $scope.isSubmitValid = true;

    $scope.$watch('adminForUpdate', function(newPasswd, oldPasswd) {
      $scope.isSubmitValid = (($scope.oldPasswdStatusClass =="has-success")
        &&($scope.passwdStatusClass =="has-success")
      &&($scope.passwdConfirmStatusClass =="has-success"));
      console.log("$scope.isSubmitValid="+$scope.isSubmitValid);
      console.log("$scope.adminUser"+newPasswd);
    },true);
    $scope.onEmailChange = function() {

    };
    $scope.onOldPasswdChange = function() {
      console.log("adminForUpdate.oldPasswd="+$scope.adminForUpdate.oldPasswd);
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      if ($scope.adminForUpdate.oldPasswd == undefined) {
        $scope.oldPasswdIconClass = "";
        $scope.oldPasswdStatusClass = "";
      } else if ($scope.adminForUpdate.oldPasswd != $scope.adminForUpdate.passwd) {
        $scope.oldPasswdIconClass = "glyphicon-remove";
        $scope.oldPasswdStatusClass = "has-error";
      } else {//if ($scope.adminUser.passwdConfirm == $scope.adminUser.passwd)
        $scope.oldPasswdIconClass = "glyphicon-ok";
        $scope.oldPasswdStatusClass = "has-success";
      }
      console.log("passwdClass="+$scope.oldPasswdIconClass+":"+$scope.oldPasswdStatusClass);
    };


    $scope.onPasswdChange = function() {
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      level = UtilsService.testPasswdPattern($scope.adminForUpdate.newPasswd, 6, 6);

      if (level == -1) {
        $scope.passwdIconClass = "glyphicon-remove";
        $scope.passwdStatusClass = "has-error";
      } else if (level == 1) {
        $scope.passwdIconClass = "glyphicon-warning-sign";
        $scope.passwdStatusClass = "has-warning";
      } else if (level == 2) {
        $scope.passwdIconClass = "glyphicon-ok";
        $scope.passwdStatusClass = "has-success";
      } else if (level == 0) {
        $scope.passwdIconClass = "";
        $scope.passwdStatusClass = "";
      }
      console.log("passwdClass="+$scope.passwdIconClass+":"+$scope.passwdStatusClass);
    };
    $scope.onPasswdConfirmChange = function() {
      console.log("adminForUpdate.newPasswdConfirm="+$scope.adminForUpdate.newPasswdConfirm);
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      if ($scope.adminForUpdate.newPasswdConfirm == undefined) {
        $scope.passwdConfirmIconClass = "";
        $scope.passwdConfirmStatusClass = "";
      } else if ($scope.adminForUpdate.newPasswdConfirm != $scope.adminForUpdate.newPasswd) {
        $scope.passwdConfirmIconClass = "glyphicon-remove";
        $scope.passwdConfirmStatusClass = "has-error";
      } else {//if ($scope.adminUser.passwdConfirm == $scope.adminUser.passwd)
        $scope.passwdConfirmIconClass = "glyphicon-ok";
        $scope.passwdConfirmStatusClass = "has-success";
      }
      console.log("passwdClass="+$scope.passwdConfirmIconClass+":"+$scope.passwdConfirmStatusClass);
    };

    $scope.onSubmit =  function() {
      console.log("adminUpdate.submit");

      adminInfo = {}
      adminInfo.userId = $scope.adminForUpdate.userId;
      adminInfo.passwd = $scope.adminForUpdate.passwd;
      adminInfo.email = $scope.adminForUpdate.email;

      StatusService.updateAdmin(adminInfo).then(function(resp) {
        if (resp.data.resultCode == "0001") { //success
          UtilsService.msgboxAlert("이메일이 변경되었습니다.");
          $rootScope.loginInfo = {};
        } else {
          console.log("login error");
          UtilsService.msgboxAlert("이메일변경중 오류가 발생했습니다.");
        }
      });
    }

    $scope.onSubmitPwd =  function() {
      console.log("adminUpdate.submitPwd");

      adminInfo = {}
      adminInfo.userId = $scope.adminForUpdate.userId;
      adminInfo.passwd = $scope.adminForUpdate.newPasswd;
      adminInfo.email = $scope.adminForUpdate.email;
      StatusService.updateAdmin(adminInfo).then(function(resp) {
        if (resp.data.resultCode == "0001") { //success
          UtilsService.msgboxAlert("패스워드가 변경되었습니다.");
          $rootScope.loginInfo = {};
        } else {
          console.log("login error");
          UtilsService.msgboxAlert("패스워드변경중 오류가 발생했습니다.");
        }
      });
    }
  }
});

app.directive('validatorHelpEx', function() {
    return {
      template : function(elem, attr) {
        console.log('validatorHelpEx');
        console.log('validatorHelpEx attr.helpStatus:'+attr.helpStatus);
        console.log('validatorHelpEx attr.helpOkMsg:'+attr.helpOkMsg);
        console.log('validatorHelpEx attr.helpWarnMsg:'+attr.helpWarnMsg);
        console.log('validatorHelpEx attr.helpErrorMsg:'+attr.helpErrorMsg);
        var retStr = "";
        if (attr.helpOkMsg  !=  undefined)
          retStr =  '<div class="help-block" ng-hide="'+attr.helpStatus+'!= undefined &&'+attr.helpStatus+'!=\'\'&&'+attr.helpStatus+'!=\'has-success\'">'+attr.helpOkMsg+'</div>';
        if (attr.helpWarnMsg  !=  undefined)
          retStr +=  '<div class="help-block" ng-hide="'+attr.helpStatus+'!=\'has-warning\'">'+attr.helpWarnMsg+'</div>';
        if (attr.helpErrorMsg  !=  undefined)
          retStr +=  '<div class="help-block" ng-hide="'+attr.helpStatus+'!=\'has-error\'">'+attr.helpErrorMsg+'</div>';
        return retStr;
      }
    };
});

app.directive('validatorHelp', function() {
    return {
      template : function(elem, attr) {
        console.log('validatorHelp');
        console.log('validatorHelp attr.helpStatus:'+attr.helpStatus);
        console.log('validatorHelp attr.helpOkMsg:'+attr.helpOkMsg);
        console.log('validatorHelp attr.helpWarnMsg:'+attr.helpWarnMsg);
        console.log('validatorHelp attr.helpErrorMsg:'+attr.helpErrorMsg);
        var retStr = "";
        if (attr.helpOkMsg  !=  undefined)
          retStr =  '<div class="help-block" ng-hide="'+attr.helpStatus+'!= undefined &&'+attr.helpStatus+'!=\'\'&&'+attr.helpStatus+'!=\'has-success\'">'+attr.helpOkMsg+'</div>';
        if (attr.helpWarnMsg  !=  undefined)
          retStr +=  '<div class="help-block" ng-hide="'+attr.helpStatus+'!=\'has-warning\'">'+attr.helpWarnMsg+'</div>';
        if (attr.helpErrorMsg  !=  undefined)
          retStr +=  '<div class="help-block" ng-hide="'+attr.helpStatus+'!=\'has-error\'">'+attr.helpErrorMsg+'</div>';
        return retStr;
      }
    };
});
