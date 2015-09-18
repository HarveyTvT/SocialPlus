/* 
 * @Author: gyl
 * @Date:   2015-09-08 11:04:28
 * @Last Modified by:   gyl
 * @Last Modified time: 2015-09-17 13:04:41
 */

$(document).ready(function(){
    var url = $(".url a").attr("href");
    var uid = new URL(url).pathname.substring(1,11);

    //user info
    $.ajax({
        url: "/userJson?uid=" + uid,
        method: "get",
        dataType: "json"
    })
    .done(function(data){
        renderUserInfo(data);
    })
    .fail(function(err){
        console.log("get user info err" + err);
    });

    //weibo info
    $.ajax({
        url: "/weiboJson?url=" + url,
        method: "get",
        dataType: "json"
    })
    .done(function(data){
        renderWeiboInfo(data);
    })
    .fail(function(err){
        console.log("get weibo info err" + err);
    });

    $.ajax({
        url: "/resultJson?url=" + url,
        method: "get",
        dataType:"json"})
        .done(function(data) {
            renderKeyUser(data);
            renderRepostPath(data);
            function drawEmotionChart(elementId) {
                var gauge = function(container, configuration) {
                    var that = {};
                    var config = {
                        size: 200,
                        clipWidth: 200,
                        clipHeight: 110,
                        ringInset: 20,
                        ringWidth: 20,

                        pointerWidth: 10,
                        pointerTailLength: 5,
                        pointerHeadLengthPercent: 0.9,

                        minValue: 0,
                        maxValue: 10,

                        minAngle: -90,
                        maxAngle: 90,

                        // transitionMs: 750,

                        majorTicks: 5,
                        labelFormat: d3.format(',g'),
                        labelInset: 10,

                        arcColorFn: d3.interpolateHsl(d3.rgb('#e75858'), d3.rgb('#00BF00'))
                    };
                    var range = undefined;
                    var r = undefined;
                    var pointerHeadLength = undefined;
                    var value = 0;

                    var svg = undefined;
                    var arc = undefined;
                    var scale = undefined;
                    var ticks = undefined;
                    var tickData = undefined;
                    var pointer = undefined;
                    var emotionText = undefined;
                    var valueText = undefined;

                    var donut = d3.layout.pie();

                    function deg2rad(deg) {
                        return deg * Math.PI / 180;
                    }

                    function newAngle(d) {
                        var ratio = scale(d);
                        var newAngle = config.minAngle + (ratio * range);
                        return newAngle;
                    }

                    function configure(configuration) {
                        var prop = undefined;
                        for (prop in configuration) {
                            config[prop] = configuration[prop];
                        }

                        range = config.maxAngle - config.minAngle;
                        r = config.size / 2;
                        pointerHeadLength = Math.round(r * config.pointerHeadLengthPercent);

                        scale = d3.scale.linear()
                            .range([0, 1])
                            .domain([config.minValue, config.maxValue]);

                        ticks = scale.ticks(config.majorTicks);
                        tickData = d3.range(config.majorTicks).map(function() {
                            return 1 / config.majorTicks;
                        });

                        arc = d3.svg.arc()
                            .innerRadius(r - config.ringWidth - config.ringInset)
                            .outerRadius(r - config.ringInset)
                            .startAngle(function(d, i) {
                                var ratio = d * i;
                                return deg2rad(config.minAngle + (ratio * range));
                            })
                            .endAngle(function(d, i) {
                                var ratio = d * (i + 1);
                                return deg2rad(config.minAngle + (ratio * range));
                            });
                    }
                    that.configure = configure;

                    function centerTranslation() {
                        return 'translate(' + r + ',' + r + ')';
                    }

                    function isRendered() {
                        return (svg !== undefined);
                    }
                    that.isRendered = isRendered;

                    function render(newValue) {
                        svg = d3.select(container)
                            .select("svg")
                            .attr('class', 'gauge')
                            .attr('width', config.clipWidth)
                            .attr('height', config.clipHeight / 2);

                        var centerTx = centerTranslation();

                        var arcs = svg.append('g')
                            .attr('class', 'arc')
                            .attr('transform', centerTx);

                        arcs.selectAll('path')
                            .data(tickData)
                            .enter().append('path')
                            .attr('fill', function(d, i) {
                                return config.arcColorFn(d * i);
                            })
                            .attr('d', arc);

                        var lg = svg.append('g')
                            .attr('class', 'label')
                            .attr('transform', centerTx);
                        lg.selectAll('text')
                            .data(ticks)
                            .enter().append('text')
                            .attr('transform', function(d) {
                                var ratio = scale(d);
                                var newAngle = config.minAngle + (ratio * range);
                                return 'rotate(' + newAngle + ') translate(0,' + (config.labelInset - r) + ')';
                            })
                            .text(config.labelFormat);

                        var lineData = [
                            [config.pointerWidth / 2, 0],
                            [0, -pointerHeadLength],
                            [-(config.pointerWidth / 2), 0],
                            [0, config.pointerTailLength],
                            [config.pointerWidth / 2, 0]
                        ];
                        var pointerLine = d3.svg.line().interpolate('monotone');
                        var pg = svg.append('g').data([lineData])
                            .attr('class', 'pointer')
                            .attr('transform', centerTx);

                        pointer = pg.append('path')
                            .attr('d', pointerLine /*function(d) { return pointerLine(d) +'Z';}*/ )
                            .attr('transform', 'rotate(' + config.minAngle + ')');

                        // emotionText = d3.select("#emotionText")
                        //     .attr('transform', 'translate(' + 0 + ',' + (-config.clipHeight / 2) + ')');

                        valueText = d3.select("#valueText");



                        update(newValue === undefined ? 0 : newValue);
                    }
                    that.render = render;

                    function update(newValue, newConfiguration) {
                        if (newConfiguration !== undefined) {
                            configure(newConfiguration);
                        }
                        var ratio = scale(newValue);
                        var newAngle = config.minAngle + (ratio * range);
                        pointer.transition()
                            .duration(config.transitionMs)
                            .ease('elastic')
                            .attr('transform', 'rotate(' + newAngle + ')');
                        valueText.transition()
                            .duration(config.transitionMs)
                            .text(newValue);
                    }
                    that.update = update;

                    configure(configuration);

                    return that;
                };

                var containerEl = document.getElementById(elementId),
                    padding = 20;
                var powerGauge = gauge(containerEl, {
                    // size: Math.min(containerEl.clientWidth,containerEl.clientHeight)-padding,
                    size: containerEl.clientWidth - padding,
                    clipWidth: containerEl.clientWidth - padding,
                    clipHeight: containerEl.clientHeight - padding,
                    ringWidth: 30,
                    maxValue: 100,
                    transitionMs: 4000,
                });
                powerGauge.render();
                powerGauge.update((data.emotion * 100).toFixed(2));
            }

            function drawTimeChart(elementId) {
                var showTime = [];
                var format = d3.time.format("%Y-%m-%d %H:%M");
                data.time.forEach(function(d) {
                    d.date = new Date(d.date);
                    showTime.push(format(d.date));
                });
                var containerEl = document.getElementById(elementId),
                    container = d3.select(containerEl),
                    margin = {
                        top: 40,
                        right: 40,
                        bottom: 40,
                        left: 40,
                    },
                    padding = 20,
                    width = containerEl.clientWidth - margin.left - margin.right - padding,
                    height = containerEl.clientHeight - margin.top - margin.bottom - padding,
                    detailWidth = 98,
                    detailHeight = 55,
                    detailMargin = 10,
                    DURATION = 1500,
                    DELAY = 500,

                    svg = container.select("svg")
                        .attr("width", width + margin.left + margin.right)
                        .attr("height", height + margin.top + margin.bottom)
                        .append("g")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")"),
                    x = d3.time.scale().range([0, width - detailWidth]),
                ////////
                    xAxisTicks = d3.svg.axis().scale(x).ticks(16).tickSize(-height).tickFormat(''),
                    y = d3.scale.linear().range([height, 0]),
                //////////
                    yAxisTicks = d3.svg.axis()
                        .scale(y).ticks(12)
                        .tickSize(width)
                        .tickFormat('').orient('right'),

                    xAxis = d3.svg.axis().scale(x).orient("bottom"),
                    yAxis = d3.svg.axis().scale(y).orient("left"),
                /////////////
                    line = d3.svg.line()
                        .interpolate('linear')
                        .x(function(d) {
                            return x(d.date) + detailWidth / 2;
                        })
                        .y(function(d) {
                            return y(d.number);
                        }),
                ////////////
                    area = d3.svg.area()
                        .interpolate('linear')
                        .x(function(d) {
                            return x(d.date) + detailWidth / 2;
                        }).y0(height).y1(function(d) {
                            return y(d.number);
                        }),
                    startData = data.time.map(function(datum) {
                        return {
                            date: datum.date,
                            number: 0
                        };
                    }),
                    circleContainer;


                x.domain(d3.extent(data.time, function(d) {
                    return d.date;
                }));
                y.domain([
                    0, (d3.max(data.time, function(d) {
                        return d.number;
                    })) + (d3.mean(data.time, function(d) {
                        return d.number;
                    }) / 4)
                ]);



                svg.append('g')
                    .attr('class', 'lineChart--xAxisTicks')
                    .attr('transform', 'translate(' + detailWidth / 2 + ',' + height + ')')
                    .call(xAxisTicks);

                svg.append("g")
                    .attr("class", "lineChart--xAxis")
                    .attr("transform", "translate(" + detailWidth / 2 + "," + (height + 7) + ")")
                    .call(xAxis);

                svg.append('g')
                    .attr('class', 'lineChart--yAxisTicks')
                    .call(yAxisTicks);

                svg.append("g")
                    .attr("class", "lineChart--yAxis")
                    .attr("transform", "translate(" + (detailWidth / 2 - 7) + "," + 0 + ")")
                    .call(yAxis);

                svg.append('path').datum(startData)
                    .attr('class', 'lineChart--areaLine')
                    .attr('d', line)
                    .transition().duration(DURATION).delay(DURATION / 2)
                    .attrTween('d', tween(data.time, line))
                    .each('end', function() {
                        drawCircles(data.time);
                    });

                svg.append('path')
                    .datum(startData)
                    .attr('class', 'lineChart--area')
                    .attr('d', area)
                    .transition()
                    .duration(DURATION)
                    .attrTween('d', tween(data.time, area));


                function drawCircle(datum, index) {
                    circleContainer.datum(datum).append('circle').attr('class', 'lineChart--circle').attr('r', 0).attr('cx', function(d) {
                        return x(d.date) + detailWidth / 2;
                    }).attr('cy', function(d) {
                        return y(d.number);
                    }).on('mouseenter', function(d) {
                        d3.select(this).attr('class', 'lineChart--circle lineChart--circle__highlighted').attr('r', 5);
                        d.active = true;
                        showCircleDetail(d, index);
                    }).on('mouseout', function(d) {
                        d3.select(this).attr('class', 'lineChart--circle').attr('r', 4);

                        if (d.active) {
                            hideCircleDetails();
                            d.active = false;
                        }
                    }).on('click touch', function(d) {
                        if (d.active) {
                            showCircleDetail(d, index);
                        } else {
                            hideCircleDetails();
                        }
                    }).transition().delay(DURATION / 10 * index).attr('r', 4);

                }

                function drawCircles(data) {
                    circleContainer = svg.append('g');
                    data.forEach(function(datum, index) {
                        drawCircle(datum, index);
                    });
                }

                function hideCircleDetails() {
                    circleContainer.selectAll('.lineChart--bubble').remove();
                }

                function showCircleDetail(data, index) {
                    var details = circleContainer.append('g').attr('class', 'lineChart--bubble').attr('transform', function() {
                        var result = 'translate(';
                        result += x(data.date);
                        result += ', ';
                        result += y(data.number) - detailHeight - detailMargin;
                        result += ')';
                        return result;
                    });
                    details.append('path')
                        .attr('d', 'M2.99990186,0 C1.34310181,0 0,1.34216977 0,2.99898218 L0,47.6680579 C0,49.32435 1.34136094,50.6670401 3.00074875,50.6670401 L44.4095996,50.6670401 C48.9775098,54.3898926 44.4672607,50.6057129 49,54.46875 C53.4190918,50.6962891 49.0050244,54.4362793 53.501875,50.6670401 L94.9943116,50.6670401 C96.6543075,50.6670401 98,49.3248703 98,47.6680579 L98,2.99898218 C98,1.34269006 96.651936,0 95.0000981,0 L2.99990186,0 Z M2.99990186,0')
                        .attr('width', detailWidth).attr('height', detailHeight);
                    var text = details.append('text').attr('class', 'lineChart--bubble--text');
                    text.append('tspan')
                        .attr('class', 'lineChart--bubble--label')
                        .attr('x', detailWidth / 2)
                        .attr('y', detailHeight / 3)
                        .attr('text-anchor', 'middle').text(showTime[index]);
                    text.append('tspan').attr('class', 'lineChart--bubble--value').attr('x', detailWidth / 2).attr('y', detailHeight / 4 * 3).attr('text-anchor', 'middle').text(data.number);
                }

                function tween(b, callback) {
                    return function(a) {
                        var i = d3.interpolateArray(a, b);
                        return function(t) {
                            return callback(i(t));
                        };
                    };
                }
            }

            function drawForceChart(elementId) {
                var containerEl = document.getElementById(elementId),
                padding = 20,
                width = containerEl.clientWidth - padding,
                height = containerEl.clientHeight - padding,
                force = d3.layout.force().charge(-80).linkDistance(50).size([width, height]);
                container = d3.select(containerEl);
                svg = container.append("svg").attr("width", width).attr("height", height).call(d3.behavior.zoom().scaleExtent([0.3, 8]).on("zoom", zoom)).append("g");;
                force.nodes(data.nodes).links(data.links).start();
                var link = svg.selectAll(".link").data(data.links).enter().append("line").attr("class", "link").style("stroke-width", 1);
                var node = svg.selectAll(".node").data(data.nodes).enter().append("circle").attr("class", "node").attr("r", function(d) {
                    return 8 - d.group;
                }).style("fill", "teal").call(force.drag);
                node.append("title").text(function(d) {
                    return d.name;
                });
                force.on("tick", function() {
                    link.attr("x1", function(d) {
                        return d.source.x;
                    }).attr("y1", function(d) {
                        return d.source.y;
                    }).attr("x2", function(d) {
                        return d.target.x;
                    }).attr("y2", function(d) {
                        return d.target.y;
                    });
                    node.attr("cx", function(d) {
                        return d.x;
                    }).attr("cy", function(d) {
                        return d.y;
                    });

                    function zoom() {
                        svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
                    }
                });
            }

            function drawForceList(elementId) {
                var containerEl = document.getElementById(elementId),
                    container = d3.select(containerEl);
                var layerList = data['layer'];
                var layer = [];
                if (layerList.length < 4){
                    for (var i = 0; i < layerList.length; i++) {
                        layer[i] = layerList[i].toFixed(2);
                    };
                }
                else{
                    var sum = 0;
                    for (var i = 0; i < 4; i++) {
                        layer[i] = layerList[i].toFixed(2);
                    };
                    for (var i = 4; i < layerList.length ; i++){
                        sum += layerList[i];
                    }
                    layer[4] = sum.toFixed(2);
                }
                var items = container.selectAll(".item")
                    .data(layer)
                    .select("span")
                    .text(function(d) {
                        return d;
                    });

            }

            function drawKeywordsChart(elementId) {
                var containerEl = document.getElementById(elementId),
                    padding = 20,
                    width = containerEl.clientWidth - padding,
                    height = containerEl.clientHeight - padding,
                    container = d3.select(containerEl),
                    fill = d3.scale.category20(),
                    layout = d3.layout.cloud()
                        .size([width, height])
                        .words(data.tags.map(function(d) {
                            return {
                                text: d,
                                size: 10 + Math.random() * 90,
                                test: "haha"
                            };
                        }))
                        .padding(5)
                        .rotate(function() {
                            return ~~(Math.random() * 2) * 90;
                        })
                        .font("Impact")
                        .fontSize(function(d) {
                            return d.size;
                        })
                        .on("end", draw);

                function draw(words) {
                    container.append("svg")
                        .attr("width", layout.size()[0])
                        .attr("height", layout.size()[1])
                        .append("g")
                        .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
                        .selectAll("text")
                        .data(words)
                        .enter().append("text")
                        .style("font-size", function(d) {
                            return d.size + "px";
                        })
                        .style("font-family", "Impact")
                        .style("fill", function(d, i) {
                            return fill(i);
                        })
                        .attr("text-anchor", "middle")
                        .attr("transform", function(d) {
                            return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                        })
                        .text(function(d) {
                            return d.text;
                        });
                }

                layout.start();

            }

            function drawMapChart(elementId) {

                var containerEl = document.getElementById(elementId),
                    padding = 20,
                    width = containerEl.clientWidth - padding,
                    height = containerEl.clientHeight - padding,
                    svg = d3.select(containerEl).select("svg")
                        .attr('class', "svg")
                        .attr("width", width)
                        .attr("height", height);


                var projection = d3.geo.mercator()
                    .center([105, 36])
                    ////////////////////////to fix
                    .scale(height * 3 / 4 + 50)
                    .translate([width / 4, height / 2]);

                var path = d3.geo.path()
                    .projection(projection);

                var barMargin = {
                        top: 40,
                        right: 20,
                        bottom: 30,
                        left: 40
                    },
                    barWidth = width / 2 - barMargin.left - barMargin.right,
                    barHeight = height * 4 / 5 - barMargin.top - barMargin.bottom;

                var x = d3.scale.ordinal()
                    .rangeRoundBands([0, barWidth], .04, 1);

                var y = d3.scale.linear()
                    .range([barHeight, 0]);

                var xAxis = d3.svg.axis()
                    .scale(x)
                    .orient("bottom");

                var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left");
                $.ajax({

                    url: "/getMap",
                    method: "get",
                    dataType:"json"})
                    .done( function(map) {


                        //if (error)
                        //    return console.error(error);
                        console.log(map.features);
                        var china = svg.append("g");

                        var provinces = china.selectAll("path")
                            .data(map.features)
                            .enter()
                            .append("path")
                            .attr("stroke", "#fff")
                            .attr("stroke-width", 1)
                            .attr("d", path);

                        var values = d3.map({
                            "00": 0
                        });
                        var names = d3.map({
                            "00": "其他"
                        });
                        //////////!!!!!!!!!!!!!!!!!!!!!!!
                        for (var i = 0; i < map.features.length; i++) {
                            var id = map.features[i].properties.id;
                            var name = map.features[i].properties.name;
                            names.set(id, name);
                            values.set(id, 0);
                        }


                        var provinceBars = svg.append("g")
                            .attr("width", barWidth + barMargin.left + barMargin.right)
                            .attr("height", barHeight + barMargin.top + barMargin.bottom)
                            .attr("transform", "translate(" + (width / 2 + 50) + "," + 50 + ")");

                        for (var n = 0; n < data.locations.length; n++) {
                            var id = data.locations[n].id;
                            var value = data.locations[n].number;
                            values.set(id, value);
                        }
                        ordinalNames = names.entries().sort(function(a, b) {
                            if (values.get(a.key) > values.get(b.key))
                                return -1;
                            if (values.get(a.key) < values.get(b.key))
                                return 1;
                            return 0;
                        });
                        var showNames = [];
                        for (var k = 0; k < ordinalNames.length; k++) {
                            if (k < 10)
                                showNames.push(ordinalNames[k]);
                            else
                                break;
                        }

                        x.domain(showNames.map(function(d) {
                            return d.value;
                        }));

                        var maxvalue = d3.max(data.locations, function(d) {
                            return d.number;
                        });
                        var minvalue = 0;
                        var lightBlue = "#dddddd";
                        var darkBlue = "#08306B";

                        var colorScale = d3.scale.linear()
                            .interpolate(d3.interpolateRgb)
                            .domain([minvalue, maxvalue])
                            .range([lightBlue, darkBlue]);


                        provinces.style("fill", function(d) {
                            return colorScale(values.get(d.properties.id));
                        })

                            .on("mouseover", function(d, i) {
                                var xPosition = d3.mouse(this)[0];
                                var yPosition = d3.mouse(this)[1];
                                d3.select("#tooltip")
                                    .style("left", xPosition + "px")
                                    .style("top", yPosition + "px")
                                    .select("#province")
                                    .text(d.properties.name)

                                d3.select("#value")
                                    .text("转发量: " + values.get(d.properties.id));
                                d3.select("#tooltip")
                                    .classed("hidden", false);

                            })
                            .on("mouseout", function(d) {
                                d3.select("#tooltip")
                                    .classed("hidden", true);
                            });

                        var defs = svg.append("defs");

                        var linearGradient = defs.append("linearGradient")
                            .attr("id", "linearColor")
                            .attr("x1", "0%")
                            .attr("y1", "0%")
                            .attr("x2", "0%")
                            .attr("y2", "100%");

                        var stop1 = linearGradient.append("stop")
                            .attr("offset", "0%")
                            .style("stop-color", darkBlue.toString());

                        var stop2 = linearGradient.append("stop")
                            .attr("offset", "100%")
                            .style("stop-color", lightBlue.toString());

                        var colorRect = svg.append("rect")
                            .attr("x", 50)
                            .attr("y", height / 2 + 50)
                            // .attr("dy,0)
                            .attr("width", 20)
                            .attr("height", 60)
                            .style("fill", "url(#" + linearGradient.attr("id") + ")");


                        var minValueText = svg.append("text")
                            .attr("class", "valueText")
                            .attr("x", 50)
                            .attr("y", height / 2 + 110)
                            .attr("dy", "1.3em")
                            .text(function() {
                                return minvalue;
                            });

                        var maxValueText = svg.append("text")
                            .attr("class", "valueText")
                            .attr("x", 50)
                            .attr("y", height / 2 + 50)
                            .attr("dy", "-0.3em")
                            .text(function() {
                                return maxvalue;
                            });
                        var explainText = svg.append("text")
                            .attr("class", "valueText")
                            .attr("x", 160)
                            .attr("y", 0)
                            .attr("dy", "-0.3em")
                            .text(function() {
                                return maxvalue;
                            });

                        y.domain([minvalue, maxvalue]);

                        provinceBars.append("g")
                            .attr("class", "x mapBarAxis")
                            .attr("transform", "translate(0," + barHeight + ")")
                            .call(xAxis);

                        provinceBars.append("g")
                            .attr("class", "y mapBarAxis")
                            .call(yAxis)
                            .append("text")
                            .attr("transform", "rotate(-90)")
                            .attr("y", 6)
                            .attr("dy", ".71em")
                            .style("text-anchor", "end")
                            .text("转发量");


                        provinceBars.selectAll(".mapBar")
                            .data(showNames)
                            .enter()
                            .append("rect")
                            .attr("class", "mapBar")
                            .attr("x", function(d) {
                                return x(d.value);
                            })
                            .attr("width", x.rangeBand())
                            .attr("y", function(d) {
                                return y(values.get(d.key));
                            })
                            .attr("height", function(d) {
                                return barHeight - y(values.get(d.key));
                            });

                    });

                //d3.json("./data/China.json",);
            }

            function drawGenderChart(elementId) {

                var containerEl = document.getElementById(elementId),
                    padding = 20,
                    width = containerEl.clientWidth - padding,
                    height = containerEl.clientHeight - padding ,
                    radius = Math.min(width*4/5, height*4/5) / 2,
                    DURATION = 1500,
                    DELAY = 500,
                    container = d3.select(containerEl),
                    svg = container.select("svg")
                        .attr("width", width)
                        .attr("height", height);


                var pie = svg.append('g').attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ')');
                var detailedInfo = svg.append('g').attr('class', 'pieChart--detailedInformation');
                var twoPi = 2 * Math.PI;
                var pieData = d3.layout.pie().value(function(d) {
                    return d.value;
                });
                var arc = d3.svg.arc().outerRadius(radius - 20).innerRadius(0);
                var pieChartPieces = pie.datum(d3.entries(data.gender)).selectAll('path').data(pieData).enter().append('path').attr('class', function(d) {
                    return 'pieChart__' + d.data.key;
                }).attr('filter', 'url(#pieChartInsetShadow)').attr('d', arc).each(function() {
                    this._current = {
                        startAngle: 0,
                        endAngle: 0
                    };
                }).transition().duration(DURATION).attrTween('d', function(d) {
                    var interpolate = d3.interpolate(this._current, d);
                    this._current = interpolate(0);
                    return function(t) {
                        return arc(interpolate(t));
                    };
                }).each('end', function handleAnimationEnd(d) {

                    if (d.data.value != 0)
                        drawDetailedInformation(d.data, this);

                });
                drawChartCenter();

                function drawChartCenter() {
                    var centerContainer = pie.append('g').attr('class', 'pieChart--center');
                    centerContainer.append('circle').attr('class', 'pieChart--center--outerCircle').attr('r', 0).attr('filter', 'url(#pieChartDropShadow)').transition().duration(DURATION).delay(DELAY).attr('r', radius - 50);
                    centerContainer.append('circle').attr('id', 'pieChart-clippy').attr('class', 'pieChart--center--innerCircle').attr('r', 0).transition().delay(DELAY).duration(DURATION).attr('r', radius - 55).attr('fill', '#fff');
                }

                function drawDetailedInformation(data, element) {
                    var bBox = element.getBBox(),
                        infoWidth = width * 0.3,
                        anchor, infoContainer, position;
                    if (bBox.x + bBox.width / 2 > 0) {

                        infoContainer = detailedInfo.append('g').attr('width', infoWidth).attr('transform', 'translate(' + (width - infoWidth) + ',' + (bBox.height + bBox.y + height / 3) + ')');
                        anchor = 'end';
                        position = 'right';
                    } else {
                        infoContainer = detailedInfo.append('g').attr('width', infoWidth).attr('transform', 'translate(' + 0 + ',' + (bBox.height + bBox.y + height / 3) + ')');

                        anchor = 'start';
                        position = 'left';
                    }
                    infoContainer.data([data.value]).append('text').text('0 ').attr('class', 'pieChart--detail--percentage').attr('x', position === 'left' ? 0 : infoWidth).attr('y', -10).attr('text-anchor', anchor).transition().duration(DURATION).tween('text', function(d) {
                        var i = d3.interpolateRound(+this.textContent.replace(/\s%/gi, ''), d);
                        return function(t) {
                            this.textContent = i(t);
                        };
                    });
                    infoContainer.append('line').attr('class', 'pieChart--detail--divider').attr('x1', 0).attr('x2', 0).attr('y1', 0).attr('y2', 0).transition().duration(DURATION).attr('x2', infoWidth);
                    infoContainer.data([data.key]).append('foreignObject').attr('width', infoWidth).attr('height', 100).append('xhtml:body').attr('class', 'pieChart--detail--textContainer ' + 'pieChart--detail__' + position).html(data.key);
                }

            }

            function drawAllChart() {
                drawTimeChart("timeChart");
                drawForceChart("forceChart");
                drawForceList("forceList");
                drawMapChart("mapChart");
                drawGenderChart("genderChart");
                drawEmotionChart("emotionChart");
                drawKeywordsChart("keywordsChart");
            }
            drawAllChart();
        })
        .fail(function(err) {
            console.log(err);
        });
});




function renderUserInfo(json){
    $(".source-name a").text(json['name']);
    $("#avatar").attr("src",json['avatar']);
    $("#followCount").text(json['followCount']);
    $("#fanCount").text(json['fanCount']);
    $("#weiboCount").text(json['weiboCount']);
}

function renderWeiboInfo(json){
    $(".text").text(json['content']);
    $(".client").text(json['client']);
    $(".repostCount").text(json['repostCount']);
    $(".commentCount").text(json['commentCount']);
}

function renderRepostPath(json){
    var keyRepost = json['repostPath'];
    var fuck = keyRepost[0]['avatar'];
    for (var i = 0; i < keyRepost.length - 1; i++) {
        var name = keyRepost[i]['name'];
        var avatar = keyRepost[i]['avatar'];
        var url = keyRepost[i]['url'];
        $("#repostPath #repostAvatar").append('<span><img src="'+avatar+'" width="50px" height="50px"/><a href="'+url +'" target="_blank">'+name+'</a></span>'
         + '<img src="/assets/images/right_arrow.png" width="50px" height="50px"/>');
    };
    var name = keyRepost[i]['name'];
    var avatar = keyRepost[i]['avatar'];
    var url = keyRepost[i]['url'];
    $("#repostPath #repostAvatar").append('<span><img src="'+avatar+'" width="50px" height="50px"/><a href="'+url+'" target="_blank">'+name+'</a></span>');
}

function renderKeyUser(json){
    var keyUser = json['keyUser'];
    var name = keyUser['name'];
    var avatar = keyUser['avatar'];
    var content = keyUser['content'];
    var datetime = Number(keyUser['datetime']);
    var repostCount = keyUser['repostCount'];
    var format = d3.time.format("%Y-%m-%d %H:%M");
    $(".bigUser-name").text(name);
    $(".bigUser-avatar").attr("src",avatar);
    $(".repost-number").text(repostCount);
    $(".repost-time").text(format(new Date(datetime)));
    $(".repost-content").text(content);
}