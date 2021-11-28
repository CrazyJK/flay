'use strict';

const theme = {
	set: (mode) => {
		$('html').attr('data-theme', mode);
	},
	dark: () => {
		theme.set('dark');
	},
	light: () => {
		theme.set('light');
	},
};

const DateUtils = {
	format: (time) => {
		return new Date(time).format('yyyy-MM-dd');
	},
};

Date.prototype.format = function (f) {
	if (!this.valueOf()) return '';

	var weekName = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
	var d = this;

	return f.replace(/(yyyy|yy|MM|dd|E|HH|hh|mm|ss|a\/p)/gi, function ($1) {
		switch ($1) {
			case 'yyyy':
				return d.getFullYear();
			case 'yy':
				return (d.getFullYear() % 1000).toString().padStart(2, '0');
			case 'MM':
				return (d.getMonth() + 1).toString().padStart(2, '0');
			case 'dd':
				return d.getDate().toString().padStart(2, '0');
			case 'E':
				return weekName[d.getDay()];
			case 'HH':
				return d.getHours().toString().padStart(2, '0');
			case 'hh':
				return ((h = d.getHours() % 12) ? h : 12).toString().padStart(2, '0');
			case 'mm':
				return d.getMinutes().toString().padStart(2, '0');
			case 'ss':
				return d.getSeconds().toString().padStart(2, '0');
			case 'a/p':
				return d.getHours() < 12 ? 'am' : 'pm';
			default:
				return $1;
		}
	});
};

var reqParam = location.search
	.split(/[?&]/)
	.slice(1)
	.map(function (paramPair) {
		return paramPair.split(/=(.+)?/).slice(0, 2);
	})
	.reduce(function (obj, pairArray) {
		obj[pairArray[0]] = pairArray[1];
		return obj;
	}, {});
