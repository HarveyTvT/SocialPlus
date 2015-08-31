User:
{
	_id: <ObjectID>,
	email: "lizhuoli1126@126.com",
	validated: true,
	password: "shsalkdfjsalkdfhasklfjhweio2341",
	token: {
		weibo:{
			token: "2.00QGSJEDwVtFsB92c29d85d90YWVlb",
			expire: 100000
		},
		renren:{
			token: "4.SDFDSFSDSDSAR14FFDSFS5d90YWVlb",
			expire: 100000
		},
		....
	result_id: [
		<ObjectID>],
		[
		<ObjectID>],
		[
		<ObjectID>],
	}
}


Result:
{
	_id: <ObjectID>,
	time: ["0":10,"1":22,"2":6......"23":14],
	gender: {
		male: 111,
		female: 222,
		other: 333
	},
	location: ["023":11,"311":22......"029":13],
	tag: ["git","shell","code"......],
	forward: [{
		"from" : "Zhuoli Li",
		"to" : "Jiaxing Yang"
	},{
		"from" : "Jiaxing Yang",
		"to" : "Haoyu Shi"
	},
	......
	{
		"from" : "Haoyu Shi",
		"to" : "Yili Gao"
	}],
	emotion: 50
}