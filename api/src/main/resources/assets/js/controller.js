/***************************************************************************************************
 ***** This file is part of RestauRate. *****
 ***** *****
 ***** Copyright (C) 2014 HoldInArms *****
 ***** *****
 ***** This program is free software: you can redistribute it and/or modify it under the       *****
 ***** terms of the GNU General Public License as published by the Free Software Foundation,   *****
 ***** either version 3 of the License, or (at your option) any later version.                 *****
 ***** *****
 ***** This program is distributed in the hope that it will be useful, but WITHOUT ANY         *****
 ***** WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A         *****
 ***** PARTICULAR PURPOSE. See the GNU General Public License for more details.                *****
 ***** *****
 ***** You should have received a copy of the GNU General Public License along with this       *****
 ***** program. If not, see <http://www.gnu.org/licenses/>. *****
 ***************************************************************************************************/

angular.module('RestaurantBlacklist.controllers', []).
controller('HomePageController', ['$rootScope', '$scope', '$state', 'RestaurantService',
	function($rootScope, $scope, $state, RestaurantService) {


		$rootScope.functionError = function() {
			$state.go('public.error');
		};
		RestaurantService.setErrorFunction($rootScope.functionError);
		$scope.restaurants = [];
		$scope.itemPerpage = 10;
		$scope.currentPage = 1;
		$scope.direction = "DESC";
		$scope.filterText = "";
		$scope.orderby = "";

		//Get all restaurants
		$scope.allRestaurants = [];
		RestaurantService.getAllRestaurants($scope.allRestaurants);

		$scope.changeDirection = function(pageChanged, rateOrVotes) {
			if (pageChanged) {
				$scope.direction = "ASC";
			} else if (rateOrVotes) {
				$scope.direction = "DESC";
			} else {
				if ($scope.direction === 'ASC') {
					$scope.direction = "DESC";
				} else {
					$scope.direction = "ASC";
				}
			}

			$scope.goToPage($scope.currentPage);
		};

		$scope.changeOrderBy = function(orderby) {
			if (orderby === $scope.orderby) {
				$scope.changeDirection();
			} else {
				$scope.orderby = orderby;
				if (orderby == "avarage" || orderby == "votes") {
					$scope.changeDirection(true, true);
				} else {
					$scope.changeDirection(true);
				}
			}
		};

		/**
		 * Go to specificted page
		 */
		$scope.goToPage = function(page) {
			if (!page) {
				$scope.currentPage = 1;
			} else if (page > $scope.allPages) {
				$scope.currentPage = 1;
			} else {
				$scope.currentPage = page;
			}

			var from = ($scope.currentPage - 1) * $scope.itemPerpage + 1;
			var to = $scope.currentPage * $scope.itemPerpage;

			if (page === $scope.allPages) {
				to = $scope.restaurantNumber;
				$scope.restaurants.splice(to - from, $scope.itemPerpage - (to - from));
			}

			if (page < 0) {
				$scope.restaurants.splice($scope.itemPerpage, (-1 * page));
				$scope.currentPage = 1;
			}

			var tmp = {};
			// Go to specified page
			RestaurantService.getRestaurants(tmp, from, to, $scope.filterText, $scope.orderby, $scope.direction, function() {
				$scope.restaurants = tmp.restaurants;
				$scope.allPages = Math.ceil(tmp.restaurantNumber / $scope.itemPerpage);
				$scope.restaurantNumber = tmp.restaurantNumber;
				$scope.filterText = "";
			});
		};

		$scope.goToPage(1);

		$scope.type = function(value) {
			if (value < 2) {
				return 'danger';
			} else if (value < 3) {
				return 'warning';
			} else if (value < 4) {
				return 'info';
			} else {
				return 'success';
			}
		};

		$scope.comment = {};

		$scope.dateOptions = {
			'year-format': "'yy'",
			'starting-day': 1
		};

		$scope.open = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			$scope.opened = true;
		};

		$scope.onTimeSetOrder = function(newDate, oldDate) {
			var d = new Date(newDate);
			$scope.comment.orderTime = d.valueOf();
		};

		$scope.onTimeSetArrive = function(newDate, oldDate) {
			var d = new Date(newDate);
			$scope.comment.arriveTime = d.valueOf();
			var dd = new Date($scope.comment.arriveTime - $scope.comment.orderTime);
			$scope.deliveryTime = dd.getUTCHours() + ":" + dd.getUTCMinutes();
		};


		$scope.isDatasCorrect = function(comment, isNewRestaurant) {
			if (isNewRestaurant) {
				$scope.missingDatas = !(angular.isDefined(comment.newRestaurantName) && !comment.newRestaurantName.length == 0 && angular.isDefined(comment) && angular.isDefined(comment.comment) && !comment.comment.length == 0 && angular.isDefined(comment.vote) && angular.isNumber(comment.vote));
			} else {
				$scope.missingDatas = !(angular.isDefined(comment) && angular.isDefined(comment.comment) && !comment.comment.length == 0 && angular.isDefined(comment.vote) && angular.isNumber(comment.vote));
			}
			return !$scope.missingDatas;
		};

		$scope.addNewRestaurant = function(comment) {
			//Check datas
			if ($scope.isDatasCorrect(comment, true)) {
				RestaurantService.newRestaurant(comment, function() {
					//Refreshing retaurant list
					$scope.restaurants = [];
					$scope.goToPage(1);
					//Close modal if list is refreshed
					$('#newRestaurant').modal('toggle');
					$scope.comment = {};
				});
			}
		};

		$scope.addNewCommentForRestaurant = function(comment, restaurant) {
			//Check datas
			if ($scope.isDatasCorrect(comment)) {
				console.log("asd");
				var postData = {};
				postData.restaurantId = restaurant.id;
				angular.forEach(comment, function(value, key) {
					postData[key] = value;
				});

				RestaurantService.newCommentForRestaurant(postData, function() {
					//Refreshing retaurant list

					$scope.restaurants = [];
					$scope.goToPage(1);
					//Close modal if list is refreshed
					$('#rateRestaurant').modal('toggle');
					$scope.comment = {};
				});
			}
		};

		/**
		 * Get comments, pagination
		 * setup default values
		 **/
		$scope.currentCommentPage = 1;
		$scope.commentPerpage = 3;

		$scope.getCommentsById = function(index, page) {
			$scope.currentRestaurant = index;
			if (!page) {
				$scope.currentCommentPage = 1;
			} else if (page > $scope.allCommentPages) {
				$scope.currentCommentPage = 1;
			} else {
				$scope.currentCommentPage = page;
			}

			var from = ($scope.currentCommentPage - 1) * $scope.commentPerpage + 1;
			var to = $scope.currentCommentPage * $scope.commentPerpage;

			if (page === $scope.allCommentPages) {
				to = $scope.commentCount;
				$scope.comments.splice(to - from, $scope.currentCommentPage - (to - from));
			}

			if (page < 0) {
				$scope.comments.splice($scope.currentCommentPage, (-1 * page));
				$scope.currentCommentPage = 1;
			}

			var tmp = {};
			$scope.comments = [];
			RestaurantService.getCommentsById($scope.restaurants[index].id, tmp, from, to, function() {
				//Adding delevery time
				angular.forEach(tmp.comments, function(value, key) {
					if (value.arriveTime && value.orderTime) {
						var d = new Date(value.arriveTime - value.orderTime);
						value.deliveryTime = d.getUTCHours() + ":" + d.getUTCMinutes();
					}
				});

				$scope.comments = tmp.comments;
				$scope.allCommentPages = Math.ceil(tmp.countComments / $scope.commentPerpage);
				$scope.commentCount = tmp.countComments;
			});
		};
	}
]).

controller('AdminPage', ['$state', 'CredentialHolder',
	function($state, CredentialHolder) {
		if (CredentialHolder.isLoggedIn()) {
			$state.go("admin.restaurants");
		} else {
			$state.go("admin.login");
		}
	}
]).

controller('AdminLoginPageController', ['$rootScope', '$scope', '$state', 'CredentialHolder', 'AdminLoginService',
	function($rootScope, $scope, $state, CredentialHolder, AdminLoginService) {
		// Redirect if already logged in.
		if (CredentialHolder.isLoggedIn()) {
			$state.go("admin.restaurants");
			return;
		}

		$('#admin_menu').hide();
		AdminLoginService.setErrorFunction(function() {
			$state.go('public.error');
		});

		$scope.login = function() {
			AdminLoginService.login($scope.username, $scope.password,
				function() {
					$('#admin_menu').show();
					$state.go("admin.restaurants");
				},

				function() {
					$scope.wronglogin = true
				});

		};
	}
]).

controller('AdminRestaurantsPageController', ['$rootScope', '$scope', '$state', 'CredentialHolder', 'RestaurantService', 'AdminRestaurantService',
	function($rootScope, $scope, $state, CredentialHolder, RestaurantService, AdminRestaurantService) {
		if (!CredentialHolder.isLoggedIn()) {
			$state.go("admin.login");
			return;
		}
		$rootScope.functionError = function() {
			$state.go('public.error');
		};

		RestaurantService.setErrorFunction($rootScope.functionError);
		$scope.restaurants = [];
		$scope.itemPerpage = 10;
		$scope.currentPage = 1;
		$scope.direction = "DESC";
		$scope.filterText = "";
		$scope.orderby = "";

		$scope.changeDirection = function(pageChanged) {
			if (pageChanged) {
				$scope.direction = "ASC";
			} else {
				if ($scope.direction === 'ASC') {
					$scope.direction = "DESC";
				} else {
					$scope.direction = "ASC";
				}
			}

			$scope.goToPage($scope.currentPage);
		};

		$scope.changeOrderBy = function(orderby) {
			if (orderby === $scope.orderby) {
				$scope.changeDirection();
			} else {
				$scope.orderby = orderby;
				$scope.changeDirection(true);
			}
		};

		/**
		 * Go to specificted page
		 */
		$scope.goToPage = function(page) {
			if (!page) {
				$scope.currentPage = 1;
			} else if (page > $scope.allPages) {
				$scope.currentPage = 1;
			} else {
				$scope.currentPage = page;
			}

			var from = ($scope.currentPage - 1) * $scope.itemPerpage + 1;
			var to = $scope.currentPage * $scope.itemPerpage;

			if (page === $scope.allPages) {
				to = $scope.restaurantNumber;
				$scope.restaurants.splice(to - from, $scope.itemPerpage - (to - from));
			}

			if (page < 0) {
				$scope.restaurants.splice($scope.itemPerpage, (-1 * page));
				$scope.currentPage = 1;
			}

			var tmp = {};
			// Go to specified page
			RestaurantService.getRestaurants(tmp, from, to, $scope.filterText, $scope.orderby, $scope.direction, function() {
				$scope.restaurants = tmp.restaurants;
				$scope.allPages = Math.ceil(tmp.restaurantNumber / $scope.itemPerpage);
				$scope.restaurantNumber = tmp.restaurantNumber;
				$scope.filterText = "";
			});
		};

		$scope.goToPage(1);

		$scope.type = function(value) {
			if (value < 2) {
				return 'danger';
			} else if (value < 3) {
				return 'warning';
			} else if (value < 4) {
				return 'info';
			} else {
				return 'success';
			}
		};

		$scope.hideRestaurant = function(index) {
			AdminRestaurantService.hide($scope.restaurants[index].id, function() {
				//Refresh list
				$scope.goToPage($scope.currentPage);
			});
		};

		$scope.reInstateRestaurant = function(index) {
			AdminRestaurantService.reInstate($scope.restaurants[index].id, function() {
				//Refresh list
				$scope.goToPage($scope.currentPage);
			});
		};

		$scope.gotoComments = function(index) {
			$state.go('admin.comments', {
				'restaurantId': $scope.restaurants[index].id
			});
		};
	}
]).

controller('AdminCommentsPageController', ['$rootScope', '$scope', '$state', '$stateParams', 'CredentialHolder', 'RestaurantService', 'AdminCommentService',
	function($rootScope, $scope, $state, $stateParams, CredentialHolder, RestaurantService, AdminCommentService) {
		if (!CredentialHolder.isLoggedIn()) {
			$state.go("admin.login");
			return;
		}

		RestaurantService.setErrorFunction($rootScope.functionError);
		//get restaurants
		$scope.restaurants = [];
		if ($stateParams.restaurantId) {
			$scope.restaurantIndex = {
				id: $stateParams.restaurantId
			};
		} else {
			$scope.restaurantIndex = {
				id: undefined
			};
		}

		$rootScope.functionError = function() {
			$state.go('public.error');
		};
		RestaurantService.getAllRestaurants($scope.restaurants, function() {
			angular.forEach($scope.restaurants, function(value, key) {
				if (value.id == $stateParams.restaurantId) {
					$scope.restaurantIndex = value;
				}
			});
		});
		AdminCommentService.setErrorFunction($rootScope.functionError);
		$scope.comments = [];
		$scope.itemPerpage = 10;
		$scope.currentPage = 1;
		$scope.direction = "DESC";
		$scope.orderby = "";

		$scope.changeDirection = function(pageChanged) {
			if (pageChanged) {
				$scope.direction = "ASC";
			} else {
				if ($scope.direction === 'ASC') {
					$scope.direction = "DESC";
				} else {
					$scope.direction = "ASC";
				}
			}

			$scope.goToPage($scope.currentPage);
		};

		$scope.changeOrderBy = function(orderby) {
			if (orderby === $scope.orderby) {
				$scope.changeDirection();
			} else {
				$scope.orderby = orderby;
				$scope.changeDirection(true);
			}
		};

		/**
		 * Go to specificted page
		 */
		$scope.goToPage = function(page) {
			if (!page) {
				$scope.currentPage = 1;
			} else if (page > $scope.allPages) {
				$scope.currentPage = 1;
			} else {
				$scope.currentPage = page;
			}

			var from = ($scope.currentPage - 1) * $scope.itemPerpage + 1;

			var to = $scope.currentPage * $scope.itemPerpage;
			if (page === $scope.allPages) {
				to = $scope.commentNumber;
				$scope.comments.splice(to - from, $scope.itemPerpage - (to - from));
			}

			if (page < 0) {
				$scope.comments.splice($scope.itemPerpage, (-1 * page));
				$scope.currentPage = 1;
			}

			var tmp = {};
			if (!$scope.restaurantIndex) {
				var to = $scope.currentPage * $scope.itemPerpage;
				$scope.restaurantIndex = {
					id: undefined
				};
			}

			// Go to specified page 
			AdminCommentService.getCommentsById(tmp, $scope.restaurantIndex.id, from, to, $scope.orderby, $scope.direction, function() {
				$scope.comments = tmp.comments;
				$scope.allPages = Math.ceil(tmp.countComments / $scope.itemPerpage);
				$scope.commentNumber = tmp.countComments;
			});
		};

		$scope.goToPage(1);

		$scope.hideComment = function(index) {
			AdminCommentService.hide($scope.comments[index].id, function() {
				//Refresh list	
				$scope.goToPage($scope.currentPage);

			});
		};

		$scope.reInstateComment = function(index) {
			AdminCommentService.reInstate($scope.comments[index].id, function() {
				//Refresh list
				$scope.goToPage($scope.currentPage);

			});
		};

		$scope.moveCommentTo = function(index, restaurantId) {
			AdminCommentService.move($scope.comments[index].id, restaurantId, function() {
				//Refresh list
				$scope.goToPage($scope.currentPage);
			})
		}

	}
]).

controller('AdminNewAdminPageController', ['$rootScope', '$scope', '$state', 'CredentialHolder', 'AdminUserService',
	function($rootScope, $scope, $state, CredentialHolder, AdminUserService) {
		if (!CredentialHolder.isLoggedIn()) {
			$state.go("admin.login");
			return;
		}

		$rootScope.functionError = function() {
			$state.go('public.error');
		};

		AdminUserService.setErrorFunction($rootScope.functionError);
		$scope.saveNewUser = function() {
			AdminUserService.addNewAdmin($scope.username, $scope.password, function(success) {
				if (success) {
					$scope.success = true;
				} else {
					$scope.fail = true;
				}
			});
		}
	}
]).

controller('AdminChangePasswordPageController', ['$rootScope', '$scope', '$state', 'CredentialHolder', 'AdminUserService',
	function($rootScope, $scope, $state, CredentialHolder, AdminUserService) {
		if (!CredentialHolder.isLoggedIn()) {
			$state.go("admin.login");
			return;
		}


		$rootScope.functionError = function() {
			$state.go('public.error');
		};

		AdminUserService.setErrorFunction($rootScope.functionError);

		$scope.changePassword = function() {
			AdminUserService.changePassword($scope.newPassword, function() {
				$scope.success = true;
			});
		}
	}
]).

controller('AdminLogoutController', ['$state', 'CredentialHolder', 'AdminLoginService',
	function($state, CredentialHolder, AdminLoginService) {
		if (!CredentialHolder.isLoggedIn()) {
			$state.go("admin.login");
			return;
		} else {
			AdminLoginService.setErrorFunction(function() {
				$state.go('public.error');
			});

			AdminLoginService.logout(function() {
				$state.go('public.home');
			});
		}
	}
]);