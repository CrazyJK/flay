'use strict';

const View = {
	flay: (opus) => {
		window.open('/ground/flay.html?opus=' + opus, 'flay-' + opus, 'width=800,height=535');
	},
	movie: (opus) => {
		$.ajax({
			url: '/ground/flay/' + opus + '/play',
		});
	},
	subtitles: (opus) => {
		$.ajax({
			url: '/ground/flay/' + opus + '/subtitles',
		});
	},
};
