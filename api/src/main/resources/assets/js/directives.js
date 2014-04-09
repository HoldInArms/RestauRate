angular.module('RestauRate.directives', [])

.directive('notNullNumber', ['$parse',
	function($parse) {
		return {
			require: 'ngModel',
			link: function(scope, elm, attrs, ctrl) {
				ctrl.$parsers.unshift(function(viewValue) {
					ctrl.$setValidity('minlength', true);
					ctrl.$setValidity('maxlength', true);
					ctrl.$setValidity('null', true);

					if (viewValue) {
						var transformedInput = viewValue.replace(/[^0-9]/g, '');

						if (transformedInput != viewValue) {
							ctrl.$setViewValue(transformedInput);
							viewValue = transformedInput;
							ctrl.$render();
						}

						if (viewValue.length < attrs.min) {
							ctrl.$setValidity('minlength', false);
							return undefined;
						}

						if (viewValue.length > attrs.maxlength) {
							ctrl.$setValidity('maxlength', false);
							return undefined;
						}

						if (viewValue < 1 || viewValue.charAt(0) == 0) {
							ctrl.$setValidity('null', false);
							return undefined;
						}


						return viewValue;
					}
				});
			}
		};
	}
]);