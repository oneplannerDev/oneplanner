console.log("appUsers.js");

app.component('appUsers', {
  templateUrl : "views/appuser/appusers.html",
  controller : function($scope,$rootScope, $stateParams, $filter,UsersService, UtilsService, PagerService ) {

    if ($stateParams.from == '' || $rootScope.usersParams == undefined) {
      $scope.searchParam = {}
      $scope.searchParam.pageIndex = 1;//$stateParams.pageIndex | 0;
      $scope.searchParam.recCntPerPage = 15;//$stateParams.recCntPerPage | 30;
      $scope.searchParam.searchWord = "";//$stateParams.searchWord | " ";
      $scope.searchParam.orderAsc = "";//userName
      $scope.searchParam.orderDesc = "sendDate";
      $scope.userNameOrderSeq = 0; // 0:
      $scope.sendDateOrderSeq = 2;
    } else {
      $scope.searchParam = $rootScope.usersParams;
    }

    $scope.pager = {};
    console.log("appUsers searchParam.pageIndex="+$scope.searchParam.pageIndex+" searchParam.searchWord="+$scope.searchParam.searchWord);

    $scope.readUsers = function (searchParam) {
      UsersService.getAllUsers(searchParam).then(function(users) {
        console.log("totalCnt"+users.totalCnt + " resultCode="+users.resultCode+" resultMsg="+users.resultMsg);
        $scope.users = users.item;
        $scope.totalCnt = users.totalCnt;

        // get pager object from service
        $scope.pager = PagerService.GetPager($scope.totalCnt, searchParam.pageIndex,$scope.searchParam.recCntPerPage);
        console.log("$filter('json')(vm.pager)"+$filter('json')($scope.pager));
            // angular.forEach (users.item, function(value, key){
            //   console.log(key+":"+value.userName+":"+value.email);
            // });
      });
    };
    $scope.strToDate = function(strDate) {
      return UtilsService.strToDate(strDate);
    };
    $scope.userType = function(str) {
      return UtilsService.userType(str);
    };

    $scope.setPage = function(page) {
      console.log("setPage="+page);
      if (page < 1 || page > $scope.pager.totalPages) {
          return;
      }
      $scope.searchParam.pageIndex = page;
      $scope.readUsers($scope.searchParam);
    }

    $scope.searchByKey = function() {
      console.log("searchByKey:"+$filter('json')($scope.searchParam));
      $scope.searchParam.pageIndex = 1;
      $scope.readUsers($scope.searchParam);
    };

    $scope.onDateChange = function(mode) {
      console.log("onDateChange="+mode+":"+$filter('json')($scope.searchParam));
      if (mode == 'from'){
        if ($scope.searchParam.dateFrom > $scope.searchParam.dateTo
          || $scope.searchParam.dateTo == ""
        || $scope.searchParam.dateTo == undefined) {
          $scope.searchParam.dateTo = $scope.searchParam.dateFrom;
        }
      } else {
        if ($scope.searchParam.dateFrom > $scope.searchParam.dateTo) {
          $scope.searchParam.dateFrom = $scope.searchParam.dateTo;
        }
      }
    };
    /** ordering*/
      // $scope.userNameOrderSeq = 0; // 0:
      // $scope.sendDateOrderSeq = 2;
    $scope.setUserNameOrder = function() {
      $scope.userNameOrderSeq+=1;
      if ($scope.userNameOrderSeq%3==0) {
        $scope.searchParam.orderAsc = "";//
        $scope.searchParam.orderDesc = "sendDate";
        $scope.sendDateOrderSeq = 2;
      } else if ($scope.userNameOrderSeq%3==1) {
        $scope.searchParam.orderAsc = "userName";//
        $scope.searchParam.orderDesc = "";
        $scope.sendDateOrderSeq = 0;
      } else if ($scope.userNameOrderSeq%3==2) {
        $scope.searchParam.orderAsc = "";//
        $scope.searchParam.orderDesc = "userName";
        $scope.sendDateOrderSeq = 0;
      }
      $scope.readUsers($scope.searchParam);
    };
    $scope.setSendDateOrder =  function() {
      $scope.sendDateOrderSeq+=1;
      if ($scope.sendDateOrderSeq%3==0) {
        $scope.searchParam.orderAsc = "userName";//
        $scope.searchParam.orderDesc = "";
        $scope.userNameOrderSeq = 1;
      } else if ($scope.sendDateOrderSeq%3==1) {
        $scope.searchParam.orderAsc = "sendDate";//
        $scope.searchParam.orderDesc = "";
        $scope.userNameOrderSeq = 0;
      } else if ($scope.sendDateOrderSeq%3==2) {
        $scope.searchParam.orderAsc = "";//
        $scope.searchParam.orderDesc = "sendDate";
        $scope.userNameOrderSeq = 0;
      }
      $scope.readUsers($scope.searchParam);
    };    
    $scope.authBtnDisable = function(authMode)  {
      return UtilsService.authBtnDisable(authMode);
    };
    $scope.resetBtnDisable = function(authMode)  {
      return UtilsService.resetBtnDisable(authMode);
    };
    $scope.mailStatus = function(authMode, authYn) {
      return UtilsService.mailStatus(authMode, authYn);
    };
    $scope.sendConfirmEmail = function(userId, langCode) {
      console.log("sendConfirmEmail");
      UsersService.sendConfirmEmail(userId, langCode).then(function(resultCode) {
            $scope.readUsers($scope.searchParam);
      });
    };
    $scope.sendResetEmail = function(userId, langCode) {
      console.log("sendResetEmail");
      UsersService.sendResetEmail(userId, langCode).then(function(resultCode) {
            $scope.readUsers($scope.searchParam);
      });
    };
    $scope.setPage($scope.searchParam.pageIndex);
  }
});

app.component('appUser', {
  templateUrl : "views/appuser/appuser_detail.html",
  controller : function($state, $scope, $stateParams,UsersService, UtilsService) {
    console.log("adminUser:"+$state.$current.url);
    $scope.appUser = {}
    $scope.from = $stateParams.from;
    console.log("adminUser $stateParams:"+$stateParams.userId);

    $scope.getUser = function() {
      UsersService.getUser($stateParams.userId).then(function(user) {
          $scope.user = user;
          $scope.mailingHistory = user.item;
          console.log("getUser name="+$scope.user.userName+":email="+$scope.user.email);
          $scope.user.roleName = UtilsService.roleName($scope.user.role);
          $scope.user.createDateP = UtilsService.strToDate($scope.user.createDate);
          $scope.user.userTypeP = UtilsService.userType($scope.user.userType);
          $scope.user.birthDateP = UtilsService.dateHypen($scope.user.birthDate);
          $scope.user.confirmDateP = UtilsService.dateHypen($scope.user.confirmDate);
      });
    };
    $scope.getUser();

    $scope.authBtnDisable = function(authMode)  {
      return UtilsService.authBtnDisable(authMode);
    };
    $scope.resetBtnDisable = function(authMode)  {
      return UtilsService.resetBtnDisable(authMode);
    };
    $scope.mailStatus = function(authMode, authYn) {
      return UtilsService.mailStatus(authMode, authYn);
    };
    $scope.sendConfirmEmail = function() {
      console.log("sendConfirmEmail");
      UsersService.sendConfirmEmail($scope.user.userId, $scope.user.langCode).then(function(resultCode) {
        $scope.getUser();
      });
    };
    $scope.sendResetEmail = function() {
      console.log("sendResetEmail");
      UsersService.sendResetEmail($scope.user.userId, $scope.user.langCode).then(function(resultCode) {
        $scope.getUser();
      });
    };


  }
});
app.component('appUserAdd', {
  templateUrl : "views/appuser/appuser_add.html",
  controller : function($state, $scope, $stateParams,UsersService, UtilsService) {
    console.log("adminUser:"+$state.$current.url);
    $scope.appUser = {}
    /*
    {name: 'appUser', url: '/appUser/view/:userId', component: 'appUser'},
    {name: 'appUserAdd', url: '/appUser/appUserAdd',  component: 'appUser'},
    {name: 'appUserUpdate', url: '/appUser/update/:userId', component: 'appUser'},
    {name: 'appUserDelete', url: '/appUser/delete/:userId', component: 'appUser'},
    */
    if ($state.$current.url.indexOf("view")!= -1) {
        console.log("appUser view $stateParams:"+$stateParams.appUserId);
    } else if ($state.$current.url.indexOf("appUserAdd")!= -1) {
      console.log("appUser add");
    } else if ($state.$current.url.indexOf("update") != -1) {
        console.log("appUser $stateParams:"+$stateParams.appUserId);
    } else if ($state.$current.url.indexOf("delete") != -1) {
        console.log("appUser $stateParams:"+$stateParams.appUserId);
    }
    //expect($stateParams).toBe({userId: });
    // UsersService.getUser($stateParams.userId).then(function(user) {
    //     $scope.user = user;
    //     console.log("getUser name="+$scope.user.userName+":email="+$scope.user.email);
    //     $scope.user.roleName = UtilsService.roleName($scope.user.role);
    //     $scope.user.createDateP = UtilsService.strToDate($scope.user.createDate);
    //     $scope.user.lastLoginTimeP = UtilsService.strToDate($scope.user.lastLoginTime);
    // });
    //$state.go("adminUser.account");
    //
  }
});
app.component('appUserUpdate', {
  templateUrl : "views/appuser/appuser_update.html",
  controller : function($state, $scope, $stateParams,UsersService, UtilsService) {
    console.log("appUserUpdate:"+$state.$current.url);
    $scope.appUser = {}
    /*
    {name: 'appUser', url: '/appUser/view/:userId', component: 'appUser'},
    {name: 'appUserAdd', url: '/appUser/appUserAdd',  component: 'appUser'},
    {name: 'appUserUpdate', url: '/appUser/update/:userId', component: 'appUser'},
    {name: 'appUserDelete', url: '/appUser/delete/:userId', component: 'appUser'},
    */
    console.log("appUser $stateParams:"+$stateParams.appUserId);
    //expect($stateParams).toBe({userId: });
    // UsersService.getUser($stateParams.userId).then(function(user) {
    //     $scope.user = user;
    //     console.log("getUser name="+$scope.user.userName+":email="+$scope.user.email);
    //     $scope.user.roleName = UtilsService.roleName($scope.user.role);
    //     $scope.user.createDateP = UtilsService.strToDate($scope.user.createDate);
    //     $scope.user.lastLoginTimeP = UtilsService.strToDate($scope.user.lastLoginTime);
    // });
    //$state.go("adminUser.account");
    //
  }
});
app.component('appUserDelete', {
  templateUrl : "views/appuser/appuser_delete.html",
  controller : function($state, $scope, $stateParams,UsersService, UtilsService) {
    console.log("appUserDelete:"+$state.$current.url);
    $scope.appUser = {}
    /*
    {name: 'appUser', url: '/appUser/view/:userId', component: 'appUser'},
    {name: 'appUserAdd', url: '/appUser/appUserAdd',  component: 'appUser'},
    {name: 'appUserUpdate', url: '/appUser/update/:userId', component: 'appUser'},
    {name: 'appUserDelete', url: '/appUser/delete/:userId', component: 'appUser'},
    */
    console.log("appUser $stateParams:"+$stateParams.appUserId);
    //expect($stateParams).toBe({userId: });
    // UsersService.getUser($stateParams.userId).then(function(user) {
    //     $scope.user = user;
    //     console.log("getUser name="+$scope.user.userName+":email="+$scope.user.email);
    //     $scope.user.roleName = UtilsService.roleName($scope.user.role);
    //     $scope.user.createDateP = UtilsService.strToDate($scope.user.createDate);
    //     $scope.user.lastLoginTimeP = UtilsService.strToDate($scope.user.lastLoginTime);
    // });
    //$state.go("adminUser.account");
    //
  }
});


app.component('adminUserAdd', {
  templateUrl : "views/admin/adminuser_add.html",
  controller : function($scope, $state, UtilsService, UsersService,RESOURCES) {
    console.log("adminUserAdd");
    $scope.newUser = {};
    $scope.isSubmitValid = true;

    $scope.$watch('newUser', function(newNames, oldNames) {
      $scope.isSubmitValid = ($scope.userIdStatusClass =="has-success")
      &&($scope.emailStatusClass =="has-success")
      &&($scope.passwdStatusClass =="has-success")
      &&($scope.passwdConfirmStatusClass =="has-success")
      ;
      console.log("$scope.isSubmitValid="+$scope.isSubmitValid);
      console.log("$scope.newUser"+newNames);
    },true);


    $scope.checkDupUserId = function(){
      console.log("$scope.newUser.userId="+$scope.newUser.userId);
      $state.transitionTo('adminUsers', {}, {
        reload: true,
        inherit: false,
        notify: true
      });
      if ($scope.newUser.userId == undefined || $scope.newUser.userId == "") return;
      var result = UsersService.getUser($scope.newUser.userId);
      console.log("RESOURCES.RESULT_SUCCESS="+RESOURCES.RESULT_SUCCESS);
      if (result.resultCode !=  RESOURCES.RESULT_SUCCESS) {
        $scope.newUser.userDupChecked = true;
        $scope.userIdStatusClass = "has-success";
      }
    };

    $scope.onUserIdChange = function() {
      console.log("newUser.userId="+$scope.newUser.userId);
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      level = UtilsService.testUserIdPattern($scope.newUser.userId, 3);
      $scope.newUser.userDupChecked = false;

      //최초: ""
      //작성중: "오류메시지"
      //조건완료: "아이디 중복 확인하세요"
      //중복확인완료: "아이디 사용가능합니다."
      //0: 초기화, -1: 조건안맞음. 2: 조건맞츰

      if (level == -1) {
        $scope.userIdIconClass = "glyphicon-remove";
        $scope.userIdStatusClass = "has-error";
      } else if (level == 2) {
        $scope.userIdIconClass = "glyphicon-ok";
        $scope.userIdStatusClass = "has-warning";
      } else if (level == 0) {
        $scope.userIdIconClass = "";
        $scope.userIdStatusClass = "";
      }
      console.log("userIdClass="+$scope.userIdIconClass+":"+$scope.userIdStatusClass);
    };
    $scope.onEmailChange = function() {
      console.log("newUser.email="+$scope.newUser.email);
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      level = UtilsService.testEmailPattern($scope.newUser.email);

      if (level == -1) {
        $scope.emailIconClass = "glyphicon-remove";
        $scope.emailStatusClass = "has-error";
      } else if (level == 2) {
        $scope.emailIconClass = "glyphicon-ok";
        $scope.emailStatusClass = "has-success";
      } else if (level == 0) {
        $scope.emailIconClass = "";
        $scope.emailStatusClass = "";
      }
      console.log("emailClass="+$scope.emailIconClass+":"+$scope.emailStatusClass);
    };
    $scope.onPasswdChange = function() {
      console.log("newUser.passwd="+$scope.newUser.passwd);
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      level = UtilsService.testPasswdPattern($scope.newUser.passwd, 6, 9);

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
      console.log("newUser.passwdConfirm="+$scope.newUser.passwdConfirm);
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      if ($scope.newUser.passwdConfirm == undefined) {
        $scope.passwdConfirmIconClass = "";
        $scope.passwdConfirmStatusClass = "";
      } else if ($scope.newUser.passwdConfirm != $scope.newUser.passwd) {
        $scope.passwdConfirmIconClass = "glyphicon-remove";
        $scope.passwdConfirmStatusClass = "has-error";
      } else {//if ($scope.newUser.passwdConfirm == $scope.newUser.passwd)
        $scope.passwdConfirmIconClass = "glyphicon-ok";
        $scope.passwdConfirmStatusClass = "has-success";
      }
      console.log("passwdClass="+$scope.passwdConfirmIconClass+":"+$scope.passwdConfirmStatusClass);
    };

    $scope.onSubmit =  function() {
      console.log("newUser.submit");
      $state.transitionTo($state.current, {}, {
        reload: true,
        inherit: false,
        notify: true
      });
      //check
      if ($scope.userIdStatusClass != "has-success" ) {
        var element = $window.document.getElementById("inputUserId");
        if (element) {
          console.log("element.focus()");
          element.focus();

        }
        return;
      }
      $scope.newUser.authYn='Y';
      $scope.newUser.deleteYn='N';
      UsersService.registerUser($scope.newUser);
    }
  }
});


//form input change대비
app.directive("formOnChange", function($parse) {
  return {
    require: "form",
    link: function(scope, element, attrs) {
      var cb = $parse(attrs.formOnChange);
      element.on("change", function(){ //change를 정의, 지정했을때
        cb(scope);
      });
    }
  }

});

app.component('adminUserEdit', {
  templateUrl : "views/admin/adminuser_edit.html",
  controller : function() {
    //
  }
});
app.component('webUsersCtrl', {
  templateUrl : "views/admin/webusers.html",
  controller : function() {
    console.log("webUsers");
  }
});
