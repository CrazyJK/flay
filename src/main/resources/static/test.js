'use strict';

let param = {
	source: 'instance',
	order: '_release,_lastmodified',
	fields: '',
	filter: '(opus = FSDSS202 or title = 며느리 or tag = 늙은이) and rank > 3',
};
$.ajax({
	url: '/ground/flay',
	data: param,
	success: (list) => {
		const size = list.length;
		list.forEach((flay, index) => {
			$('#list').append(
				`<li class="flay">
					<label class="flay-no">${size - index}</label>
					<label class="flay-studio">${flay.studio.name}</label>
					<label class="flay-opus">${flay.opus}</label>
					<label class="flay-title">${flay.title}</label>
					<label class="flay-actress">${flay.actress.map((a) => a.name).join(',')}</label>
					<label class="flay-release">${flay.release}</label>
					<label class="flay-modified">${DateUtils.format(flay.modified)}</label>
					<label class="flay-rank">${flay.video.rank}</label>
					<label class="flay-play">${flay.video.play}</label>
				</li>`,
			);
		});

		$('#flayCount').html(size);
		$('#source').html(param.source);
		$('#filter').html(param.filter);
		$('#fields').html(param.fields);
		$('#order').html(param.order);
	},
});
