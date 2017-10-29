console.log("todays.js");

app.component('todays', {
  templateUrl : "views/today/todays.html",
  controller : function($scope,$rootScope, $stateParams, $filter,TodayService, UtilsService, PagerService, RESOURCES ) {
    if ($stateParams.from == '' || $rootScope.todaysParams == undefined) {
      $scope.searchParam = {}
      $scope.searchParam.pageIndex = 1;//$stateParams.pageIndex | 0;
      $scope.searchParam.recCntPerPage = 15;//$stateParams.recCntPerPage | 30;
      $scope.searchParam.searchWord = "";//$stateParams.searchWord | " ";
    } else {
      $scope.searchParam = $rootScope.todaysParams;
    }
    console.log("todays searchParam.pageIndex="+$scope.searchParam.pageIndex+" searchParam.searchWord="+$scope.searchParam.searchWord);

    $scope.readTodays = function (searchParam) {
      TodayService.getAllTodays(searchParam).then(function(todays) {
        console.log("totalCnt"+todays.totalCnt + " resultCode="+todays.resultCode+" resultMsg="+todays.resultMsg);
        $scope.todays = todays.item;
        $scope.totalCnt = todays.totalCnt;

        // get pager object from service
        $scope.pager = PagerService.GetPager($scope.totalCnt, searchParam.pageIndex,$scope.searchParam.recCntPerPage);
        console.log("$filter('json')(vm.pager)"+$filter('json')($scope.pager));
            // angular.forEach (users.item, function(value, key){
            //   console.log(key+":"+value.userName+":"+value.email);
            // });
      });
    };
    $scope.fullUrl = function(type, url) {
      return UtilsService.fullUrl(type,url);
    };
    $scope.strToDate = function(strDate) {
      return UtilsService.strToDate(strDate);
    };
    $scope.openedId = "";

    $scope.setOpenedId = function(id) {
      $scope.openedId = id;
      //console.log("$scope.openedId="+$scope.openedId);
    };

    $scope.showPopover = false;
    $scope.showImagePopup = function(url) {
      if (url == null) return;
      $scope.showPopover = true;
    };

    $scope.closeImagePopup = function() {
      $scope.showPopover=false;
    }
    $scope.pager = {};
    $scope.setPage = function(page) {
      console.log("setPage="+page);
      if (page < 1 || page > $scope.pager.totalPages) {
          return;
      }
      $scope.searchParam.pageIndex = page;
      $scope.readTodays($scope.searchParam);
    }

    $scope.searchByKey = function() {
      console.log("searchByKey:"+$filter('json')($scope.searchParam));
      $scope.searchParam.pageIndex = 1;
      $scope.readTodays($scope.searchParam);
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
    $scope.deleteToday = function(today, contSeq) {
      param = {};
      param.today  = today;
      param.contSeq = contSeq;
      TodayService.deleteToday(param, function() {$scope.setPage($scope.searchParam.pageIndex);});
    };

    $scope.setPage($scope.searchParam.pageIndex);
  }
});

app.component('today', {
  templateUrl : "views/today/today_detail.html",
  controller : function($state, $scope, $stateParams,$filter, TodayService, UtilsService, RESOURCES) {
    $scope.today = {}
    console.log("today $stateParams:"+$stateParams.today+":"+$stateParams.contSeq);
    $scope.today.today = $stateParams.today
    $scope.today.contSeq = $stateParams.contSeq;

    TodayService.getToday($scope.today).then(function(today) {
        $scope.today = today;
        console.log("getToday :" + $filter('json')(today));
    });
    $scope.deleteToday = function() {
      TodayService.deleteToday($scope.today,function(){
        $state.transitionTo('todays'//$rootScope.previousState
          , {from:'today'}, {reload: true,inherit: false,notify: true});
      });
    };

  }
});

app.component('todayAdd', {
  templateUrl : "views/today/today_add.html",
  controller : function($state,$rootScope, $scope, $stateParams,TodayService, UtilsService) {
    $scope.filepreview ="";
    $scope.urlpreview = "";
    console.log("todayAdd:"+$state.$current.url);
    $scope.today = {today:UtilsService.todayHypen(),content:""}
    $scope.$watch('file', function(newfile, oldfile) {
      if(angular.equals(newfile, oldfile) ){
        return;
      }
      console.log("watching");
      $scope.upfile = newfile;
    });
    $scope.addToday = function() {
      //upload file and text
      $scope.today.imageType = ($scope.selectedTabName == 'byFile')?'I':'O';
      TodayService.registerToday($scope.today, $scope.upfile).then(function(res){
        // DO SOMETHING WITH THE RESULT!
        console.log("result", res);
        if (res) {
          $state.transitionTo('todays'//$rootScope.previousState
            , {from:'todayAdd'}, {reload: true,inherit: false,notify: true});
        } else {
          UtilsService.msgboxAlert("등록오류가 발생했습니다.");
        }
      });
    };
    $scope.onContentChange  = function() {
      console.log("onContentChange");
    };

    $scope.selectedTabName = "byUrl";
    $scope.onTabSelect = function(tabName) {
      $scope.selectedTabName = tabName;
      console.log("selectedTabName:"+$scope.selectedTabName);
    }

    $scope.onImgUrlChange = function() {
      console.log("onImgUrlChange:"+$scope.today.imageUrl);
      //if ($scope.today.imageUrl.includes("http://") || $scope.today.imageUrl.includes("https://")) return;
      UtilsService.isImage($scope.today.imageUrl).then(function(data){
        console.log("isImage:"+data);
        if (data != false) {
          console.log("displaying");
          $scope.urlpreview=$scope.today.imageUrl;
          $scope.needUrlPreview=true;
        }
      });
    };

  }
});
app.component('todayUpdate', {
  templateUrl : "views/today/today_update.html",
  controller : function($state, $scope, $filter, $stateParams,TodayService,RESOURCES, UtilsService) {
    console.log("todayUpdate:"+$state.$current.url);
    $scope.$watch('file', function(newfile, oldfile) {
      if(angular.equals(newfile, oldfile) ){
        return;
      }
      console.log("watching");
      $scope.upfile = newfile;
    });

    $scope.selectedTabName = "byUrl";
    $scope.onTabSelect = function(tabName) {
      $scope.selectedTabName = tabName;
      console.log("selectedTabName:"+$scope.selectedTabName);
    }

    $scope.today = {}
    console.log("today $stateParams:"+$stateParams.today+":"+$stateParams.contSeq);
    $scope.today.today = $stateParams.today
    $scope.today.contSeq = $stateParams.contSeq;

    TodayService.getToday($scope.today).then(function(today) {
        $scope.today = today;
        if ($scope.today.imageType == 'I') {
          $scope.selectedTabName = 'byFile';
          $scope.fileClass="active";
          $scope.urlClass="";

        } else {
          $scope.selectedTabName = 'byUrl';
          $scope.fileClass="";
          $scope.urlClass="active";
        }
        console.log("getToday :"+$filter('json')(today));
    });
    $scope.updateToday = function() {
      if ($scope.selectedTabName == 'byFile') {
        $scope.today.imageType = 'I';
      } else {
        $scope.today.imageType = 'O';
      }
      TodayService.updateToday($scope.today, $scope.upfile).then(function(res){
        // DO SOMETHING WITH THE RESULT!
        console.log("result", res);
        $state.transitionTo('todays'
          //$rootScope.previousState
          , {from:'todayUpdate'}, {
          reload: true,
          inherit: false,
          notify: true
        });
      });
    };
    $scope.deleteToday = function() {
      TodayService.deleteToday($scope.today).then(function(res){
        // DO SOMETHING WITH THE RESULT!
        console.log("result", res);
        $state.transitionTo('todays'
          //$rootScope.previousState
          , {from:'todayUpdate'}, {
          reload: true,
          inherit: false,
          notify: true
        });
      });
    };
  }
});

app.directive("fileinput", [function() {
    return {
      scope: {
        fileinput: "=",
        filepreview: "="
      },
      link: function(scope, element, attributes) {
        element.bind("change", function(changeEvent) {
          scope.fileinput = changeEvent.target.files[0];
          var reader = new FileReader();
          reader.onload = function(loadEvent) {
            scope.$apply(function() {
              scope.filepreview = loadEvent.target.result;
            });
          }
          reader.readAsDataURL(scope.fileinput);
        });
      }
    }
  }]);
