app.component('introStat', {
  //template:  '<h3>{{$ctrl.greeting}} galaxy!</h3>',
  templateUrl: "views/intro.html",
  controller: function($rootScope, $state, $scope,$filter, $stateParams,StatusService, PagerService,UtilsService, UsersService) {
    console.log('intro controller'+$state.$current.url);
    console.log('intro controller'+$stateParams.from);

    if ($stateParams.from == undefined || $stateParams.from == ''
    || $rootScope.introParams == undefined) {
      $scope.searchParam = {}
      $scope.searchParam.pageIndex = 1;//$stateParams.pageIndex | 0;
      $scope.searchParam.recCntPerPage = 15;//$stateParams.recCntPerPage | 30;
      $scope.searchParam.orderAsc = "";//userName
      $scope.searchParam.orderDesc = "sendDate";
      $scope.userNameOrderSeq = 0; // 0:
      $scope.sendDateOrderSeq = 2;
    } else {
      $scope.searchParam = $rootScope.introParams;
    }

    $scope.pager = {};

    $scope.getStatus = function() {
      StatusService.getStatus().then(function(status) {
        $scope.totalUserCnt             = status.totalUserCnt;
        $scope.emailUserCnt             = status.emailUserCnt;
        $scope.googleUserCnt            = status.googleUserCnt;
        $scope.authUnconfirmCnt         = status.authUnconfirmCnt;
        $scope.resetUnconfirmCnt        = status.resetUnconfirmCnt;
        $scope.recentUserCntByMonth     = status.recentUserCntByMonth;
        $scope.recentUserCntByWeek      = status.recentUserCntByWeek;
        $scope.recentUserCntByDay       = status.recentUserCntByDay;
        $scope.unconfirmedUserCntByWeek = status.unconfirmedUserCntByWeek;
        console.log("$filter('json')($scope.searchParam)"+$filter('json')($scope.searchParam));
        if ($scope.searchParam.mode != undefined) {
          $scope.getUsers($scope.searchParam.mode);
        }
      });
    };

    $scope.getUsers =  function(mode) {
      console.log("users");
      console.log("$filter('json')($scope.searchParam)"+$filter('json')($scope.searchParam));
      $scope.searchParam.mode = mode;
      if (mode == 'totalUserCnt') $scope.searchParam.totalcnt= $scope.totalUserCnt;
      if (mode == 'emailUserCnt') $scope.searchParam.totalcnt= $scope.emailUserCnt;
      if (mode == 'googleUserCnt') $scope.searchParam.totalcnt= $scope.googleUserCnt;
      if (mode == 'authUnconfirmCnt') $scope.searchParam.totalcnt= $scope.authUnconfirmCnt;
      if (mode == 'resetUnconfirmCnt') $scope.searchParam.totalcnt= $scope.resetUnconfirmCnt;
      if (mode == 'recentUserCntByMonth') $scope.searchParam.totalcnt= $scope.recentUserCntByMonth;
      if (mode == 'recentUserCntByWeek') $scope.searchParam.totalcnt= $scope.recentUserCntByWeek;
      if (mode == 'recentUserCntByDay') $scope.searchParam.totalcnt= $scope.recentUserCntByDay;
      if (mode == 'unconfirmedUserCntByWeek') $scope.searchParam.totalcnt= $scope.unconfirmedUserCntByWeek;


      StatusService.getUsers($scope.searchParam).then(function(data) {
        $scope.users  = data.item;
        $scope.totalCnt = $scope.searchParam.totalcnt;
        //console.log("users.length = "+$scope.users.length);
        $scope.pager = PagerService.GetPager($scope.totalCnt, $scope.searchParam.pageIndex,$scope.searchParam.recCntPerPage);
        console.log("$filter('json')(vm.pager)"+$filter('json')($scope.pager));
      });
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
      if ($scope.searchParam.mode != undefined) {
        $scope.getUsers($scope.searchParam.mode);
      }
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
      if ($scope.searchParam.mode != undefined) {
        $scope.getUsers($scope.searchParam.mode);
      }
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
      UsersService.sendConfirmEmail(userId, langCode);
    };
    $scope.sendResetEmail = function(userId, langCode) {
      console.log("sendResetEmail");
      UsersService.sendResetEmail(userId, langCode);
    };
    $scope.setPage = function(page) {
      console.log("setPage="+page);
      if (page < 1 || page > $scope.pager.totalPages) {
          return;
      }
      $scope.searchParam.pageIndex = page;
      $scope.getUsers($scope.searchParam.mode);
    }

    $scope.getStatus();
  }
})
