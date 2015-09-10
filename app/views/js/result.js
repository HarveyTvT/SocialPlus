/* 
 * @Author: gyl
 * @Date:   2015-09-08 11:04:28
 * @Last Modified by:   gyl
 * @Last Modified time: 2015-09-10 20:38:33
 */

(function() {
    // $('.ui.embed').embed();
    d3.json("data/test.json", function(data) {
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
                        .attr('height', config.clipHeight / 2 + 30);

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
            powerGauge.update(data.emotion);
        }

        function drawTimeChart(elementId) {
            // var showTime = [];
            // var format = d3.time.format("%Y-%m-%d");
            data.time.forEach(function(d) {
                d.date = new Date(d.date);
                // showTime.push(format(d.date));
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
                ///////////////
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
                }).on('mouseenter', function(d, i) {
                    d3.select(this).attr('class', 'lineChart--circle lineChart--circle__highlighted').attr('r', 7);
                    d.active = true;
                    showCircleDetail(d, i);
                }).on('mouseout', function(d) {
                    d3.select(this).attr('class', 'lineChart--circle').attr('r', 6);
                    if (d.active) {
                        hideCircleDetails();
                        d.active = false;
                    }
                }).on('click touch', function(d, i) {
                    if (d.active) {
                        showCircleDetail(d, i);
                    } else {
                        hideCircleDetails();
                    }
                }).transition().delay(DURATION / 10 * index).attr('r', 6);
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
                // .attr('text-anchor', 'middle').text( +
                //     showTime[index]);
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
                force = d3.layout.force().charge(-120).linkDistance(30).size([width, height]);
            container = d3.select(containerEl);
            svg = container.append("svg").attr("width", width).attr("height", height);
            force.nodes(data.nodes).links(data.links).start();
            var link = svg.selectAll(".link").data(data.links).enter().append("line").attr("class", "link").style("stroke-width", 1);
            var node = svg.selectAll(".node").data(data.nodes).enter().append("circle").attr("class", "node").attr("r", function(d) {
                return 5 + d.group;
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
            });
        }

        function drawKeywordChart(elementId) {
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
                .scale(width * 3 / 4)
                .translate([width / 2, height / 2]);

            var path = d3.geo.path()
                .projection(projection);

            d3.json("./data/China.json", function(error, map) {

                if (error)
                    return console.error(error);
                console.log(map.features);
                var china = svg.append("g");

                var provinces = china.selectAll("path")
                    .data(map.features)
                    .enter()
                    .append("path")
                    .attr("stroke", "#fff")
                    .attr("stroke-width", 1)
                    .attr("d", path);

                var values = [];
                for (var i = 0; i < map.features.length; i++) {
                    var id = map.features[i].properties.id;
                    var number = 0;
                    values[id] = 0;
                }


                for (var m = 0; m < data.locations.length; m++) {
                    var id = data.locations[m].id;
                    var number = data.locations[m].number;
                    values[id] = number;
                }

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


                provinces.style("fill", function(d, i) {
                        return colorScale(values[d.properties.id]);
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
                            .text("转发量: " + values[d.properties.id]);
                        d3.select("#tooltip")
                            .classed("hidden", false);

                    })
                    .on("mouseout", function(d) {
                        d3.select("#tooltip")
                            .classed("hidden", true);
                    });
            });
        }

        function drawGenderChart(elementId) {
            var containerEl = document.getElementById(elementId),
                padding = 20,
                width = containerEl.clientWidth - padding,
                height = containerEl.clientHeight - padding,
                container = d3.select(containerEl),
                svg = container.append("svg")
                .attr("width", width)
                .attr("height", height);


            var outerRadius = Math.min(width, height) / 2;
            var innerRadius = 0;
            var pie = d3.layout.pie()
                .sort(null);
            var arc = d3.svg.arc()
                .innerRadius(innerRadius)
                .outerRadius(outerRadius);

            var color = d3.scale.category20c();
            var path = svg.selectAll("g")
                .data(pie(d3.values(data.gender)))
                .enter()
                .append("g")
                .attr("transform", "translate(" + outerRadius + "," + outerRadius + ")");
            path.append("path")
                .attr("fill", function(d, i) {
                    return color(i);
                })
                .attr("d", arc);
            var gender_text = path.append("text")
                .attr("transform", function(d) {
                    return "translate(" + arc.centroid(d) + ")";
                })
                .attr("dy", "1em")
                .attr("text-anchor", "middle")
                .attr("fill", "white")
                .text(function(d) {
                    return d.value;
                });


            var value_text = path.append("text")
                .attr("transform", function(d) {
                    return "translate(" + arc.centroid(d) + ")";
                })
                .attr("dy", "-0.4em")
                .attr("text-anchor", "middle")
                .attr("fill", "white")
                .text(function(d, i) {
                    return d3.keys(data.gender)[i];



                });
        }

        function drawAllChart() {
            drawEmotionChart("emotionChart");
            drawTimeChart("timeChart");
            drawForceChart("forceChart");
            drawKeywordChart("keywordChart");
            drawMapChart("mapChart");
            drawGenderChart("genderChart");

        }
        drawAllChart();
    });
}());