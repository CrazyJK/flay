'use strict';

let param = {
	source: 'instance',
	order: '_release,_lastmodified',
	fields: '',
	filter: '(opus = FSDSS202 or title = 며느리 or tag = 늙은이) and rank > 3',
};

const resolveFlay = (list) => {
	const size = list.length;
	list.forEach((flay, index) => {
		$('#list').append(
			`<li class="flay">
				<label class="flay-no">${size - index}</label>
				<label class="flay-studio">${flay.studio.name}</label>
				<label class="flay-opus">${flay.opus}</label>
				<label class="flay-title">
					<a class="flay-title-link" href="javascript:View.flay('${flay.opus}')">${flay.title}</a>
					<span class="flay-tag">${resolveTag(flay.video.tags)}</span>
					<span class="flay-comment">${flay.video.comment}</span>
				</label>
				<label class="flay-actress">${resolveActress(flay.actress)}</label>
				<label class="flay-release">${flay.release}</label>
				<label class="flay-modified">${DateUtils.format(flay.modified)}</label>
				<label class="flay-rank">${flay.video.rank}</label>
				<label class="flay-play">${flay.video.play}</label>
				<label class="flay-files">${flay.files.MOVIE.length} : ${flay.files.SUBTITLES.length}</label>
			</li>`,
		);
	});

	$('#flayCount').html(size);
	$('#source').html(param.source);
	$('#filter').html(param.filter);
	$('#fields').html(param.fields);
	$('#order').html(param.order);
};

const resolveActress = (list) => {
	return list
		.map(
			(a) => `<span class="flay-actress-item ${a.favorite ? 'favorite' : ''}"
						title="${a.name} ${a.localName} ${a.birth} ${a.debut} ${a.body} ${a.height}">${a.name}</span>`,
		)
		.join('');
};

const resolveTag = (list) => {
	return list.map((t) => `<span class="flay-tag-item" title="${t.description}">${t.name}</span>`).join('');
};

$('#theme').on('change', (e) => {
	console.log(e.target.checked);
	e.target.checked ? theme.light() : theme.dark();
});

$.ajax({
	url: '/ground/flay',
	data: param,
	success: resolveFlay,
});
