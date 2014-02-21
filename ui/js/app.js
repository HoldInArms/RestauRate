var RestaurantBlacklist = angular.module('RestaurantBlacklist', [
    "RestaurantBlacklist.controllers",
    "RestaurantBlacklist.services",
    "ui.router",
    "ui.bootstrap",
    "ui.bootstrap.datetimepicker"
    
]);

RestaurantBlacklist.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/home"),
    $stateProvider

    // --------------- PUBLIC ZONE --------------------            
    .state('public', {
        url: "",
        abstract: true,
        template: "<div ui-view></div>"
    })

    .state('public.home', {
        url: "/home",
        templateUrl: "partials/home.html",
        controller: 'HomePageController'
    })

    .state('public.error', {
        url: "/error",
        templateUrl: "partials/error.html"
    })

    // --------------- ADMIN ZONE --------------------       
    .state('admin', {
        url: "",
        abstract: true,
        templateUrl: "partials/admin/index.html"
    })
         
    .state('admin.home', {
        url: "/admin",
        controller: 'AdminPage'
    })

    .state('admin.login', {
        url: "/admin/login",
        templateUrl: "partials/admin/login.html",
        controller: 'AdminLoginPageController'
    })

    .state('admin.restaurants', {
        url: "/admin/restaurants",
        templateUrl: "partials/admin/restaurants.html",
        controller: 'AdminRestaurantsPageController'
    })

    .state('admin.comments', {
        url: "/admin/comments/:restaurantId",
        templateUrl: "partials/admin/comments.html",
        controller: 'AdminCommentsPageController'
    })

    .state('admin.new-admin', {
        url: "/admin/new-admin",
        templateUrl: "partials/admin/new-admin.html",
        controller: 'AdminNewAdminPageController'
    })

    .state('admin.change_password', {
        url: "/admin/change-password",
        templateUrl: "partials/admin/change_password.html",
        controller: 'AdminChangePasswordPageController'
    });
});