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
		};

		$scope.addNewRestaurant = function(comment) {
			RestaurantService.newRestaurant(comment, function() {
				//Refreshing retaurant list
				$scope.restaurants = [];
				$scope.goToPage(1);
				//Close modal if list is refreshed
				$('#newRestaurant').modal('toggle');
				$scope.comment = {};
			});
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
				$scope.comments = tmp.comments;
				$scope.allCommentPages = Math.ceil(tmp.countComments / $scope.commentPerpage);
				$scope.commentCount = tmp.countComments;
				var dd = new Date($scope.comment.arrive - $scope.comment.order);
				$scope.deliveryTime = dd.getUTCHours() + ":" + dd.getUTCMinutes();
			});
		};

		$scope.addNewCommentForRestaurant = function(comment, restaurant) {
			var postData = {};
			postData.restaurant = restaurant;
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
		};

	}
]);