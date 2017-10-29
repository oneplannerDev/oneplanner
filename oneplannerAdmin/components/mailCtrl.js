app.controller("mailCtrl",['$scope','$state','$rootScope','$location','$http','$filter','UsersService','StatusService',function($scope,$state,$rootScope,$location,$http,$filter, UsersService,StatusService) {

  console.log("mailCtrl");
}]);

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
          , {}, {reload: true,inherit: false,notify: true});
      });
    };
  }
});


app.directive('onEnterKey', function() {
  console.log("onMyEnterKey");
 return function(scope, element, attrs) {
    element.bind("keydown keypress", function(event) {
      var keyCode = event.which || event.keyCode;
        if (keyCode == 13) {
          scope.$apply(function() {
              scope.$eval(attrs.onEnterKey);
          });
          event.preventDefault();
        }
    });
  };

});
