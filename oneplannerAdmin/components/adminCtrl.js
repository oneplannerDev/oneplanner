app.controller("adminCtrl",['$scope','$state','$rootScope','$location','$http','$filter','UsersService','StatusService',function($scope,$state,$rootScope,$location,$http,$filter, UsersService,StatusService) {
  //console.log($filter('json')($rootScope.loginInfo));
  $scope.setLog = function() {
    console.log("setLog");
    StatusService.ClearCredentials();
    //$rootScope.loginInfo = {};
  }
}]);

app.controller("profileCtrl", function($scope, $routeParams){
  $scope.profileEditing=false;
  $scope.changeTabToProfile = function(tabMode) {
    if (tabMode != undefined) {
      $scope.profileMode=tabMode;
    }
    $scope.tabUpperAccount=($scope.profileMode=="account")?"active":"";
    $scope.tabUpperProfile=($scope.profileMode=="profile")?"active":"";
    $scope.tabUpperHistory=($scope.profileMode=="history")?"active":"";
    if ($scope.profileEditing) {
      $scope.tabBodyAccount="";
      $scope.tabBodyProfile="";
      $scope.tabBodyHistory="";
      $scope.tabBodyAccountEdit=($scope.profileMode=="account")?"in active":"";
      $scope.tabBodyProfileEdit=($scope.profileMode=="profile")?"in active":"";
      $scope.tabBodyHistoryEdit=($scope.profileMode=="history")?"in active":"";
    } else {
      $scope.tabBodyAccount=($scope.profileMode=="account")?"in active":"";
      $scope.tabBodyProfile=($scope.profileMode=="profile")?"in active":"";
      $scope.tabBodyHistory=($scope.profileMode=="history")?"in active":"";
      $scope.tabBodyAccountEdit="";
      $scope.tabBodyProfileEdit="";
      $scope.tabBodyHistoryEdit="";
    }
    console.log("$scope.profileMode="+$scope.profileMode);
  };
  $scope.changeTabOnEdit = function(tabMode, editMode) {
    if (mode != undefined) {
      $scope.profileEditing=mode;
    }
    $scope.changeTabToProfile();
  }
  $scope.changeTabToProfile($routeParams["profile"]);
});

// app.controller("historyCtrl", function($scope){
//   $scope.profileMode="history";
//   console.log("$scope.profileMode="+$scope.profileMode);
// });
  // $scope.tabStatusAccount=(mode=="account")?"active":"";
  // $scope.pageStatusAccount=(mode=="account")?" in active":"";
  //
  // $scope.tabStatusProfile=(mode=="profile")?"active":"";
  // $scope.pageStatusProfile=(mode=="profile")?" in active":"";
  //
  // $scope.tabStatusHistory=(mode=="history")?"active":"";
  // $scope.pageStatusHistory=(mode=="history")?" in active":"";
app.controller("snsCtrl", ['$scope','$rootScope','$filter',function($scope,$rootScope,$filter){
  console.log($filter('json')($rootScope));
}]);

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
