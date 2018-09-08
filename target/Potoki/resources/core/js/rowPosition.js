var Bar = reactChartjs2.HorizontalBar;

numeral.register('locale', 'ru', {
    delimiters: {
        thousands: ' ',
        decimal: '.'
    },
    abbreviations: {
        thousand: 'тыс.',
        million: 'млн.',
        billion: 'млрд.',
        trillion: 'трлд.'
    },
    ordinal : function (number) {
        return number === 1 ? 'ый' : 'ый';
    },
    currency: {
        symbol: 'руб.'
    }
});

numeral.locale('ru');

let localData = {
    userData: {
        place: 13,
        value: 67000.53913959135
    },
    otherDatas: [
        {
            name: "Инвестор 37",
            value: 220000.62345234
        },
        {
            name: "Инвестор 72",
            value: 214000.623452345
        },
        {
            name: "Инвестор 01",
            value: 201000.7252323
        },
        {
            name: "Инвестор 92",
            value: 186000.32432234
        },
        {
            name: "Инвестор 09",
            value: 179000.53234234
        },
        {
            name: "Инвестор 02",
            value: 160000.12351235
        },
        {
            name: "Инвестор 23",
            value: 159000.51235215
        },
        {
            name: "Инвестор 34",
            value: 150000.1234512
        },
        {
            name: "Инвестор 12",
            value: 142000.51325123
        },
        {
            name: "Инвестор 51",
            value: 141000.12351235
        },
        {
            name: "Инвестор 22",
            value: 100000.232351325
        },
        {
            name: "Инвестор 44",
            value: 85000.240989248209
        },
        {
            name: "Инвестор 21",
            value: 67000.53913959135
        },

    ]
}

class ChartVertBar extends React.Component {
    constructor(props){
        super(props)

        this.handleChangePlace = this.handleChangePlace.bind(this);
        this.handleChangeVolume = this.handleChangeVolume.bind(this);

        this.state = {
            data: {
                userData: {
                    place: 1,
                    value: 0
                },
                otherDatas: [
                    {
                        name: "Я",
                        value: 0
                    }
                ]
            },
            wishPlace: {
                place: 1,
                value: 0
            },
            inputs: {
                place: 1,
                value: 0
            },
            currentPosition: 0,
            lastValue: ''
        }
    }

    componentWillMount() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        let self = this;

        $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: "getRating",
            data: "",
            dataType: 'json',
            timeout: 100000,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                let parseData = JSON.parse(data);
                self.setState({
                    data: parseData,
                    wishPlace: parseData.userData,
                    inputs: parseData.userData,
                    currentPosition: (parseData.userData.value || '').length,
                    lastValue: (parseData.userData.value || '')
                });
            },
            error: function () {
                $.getJSON("json/getRating.json", function(data) {
                    self.setState({
                        data: data,
                        wishPlace: data.userData,
                        inputs: data.userData,
                        currentPosition: (data.userData.value || '').length,
                        lastValue: (data.userData.value || '')
                    });
                    //console.log('Произошла ошибка - ' + e.toString());
                });
            }
        });
    }

    componentDidUpdate () {
        if (this.state.currentPosition === undefined)
            return
        var pos = this.state.currentPosition;
        if (this.state.lastValue.split(' ').length > this.refs.inputValue.value.split(' ').length)
            pos--;
        else if (this.state.lastValue.split(' ').length < this.refs.inputValue.value.split(' ').length)
            pos++;

        this.refs.inputValue.setSelectionRange(pos, pos);
    }

    formatData(userData) {

        let valuesCount = 3;
        let realData = this.state.data.userData;
        let { otherDatas } = this.state.data;
        var isNull = false;
        var datas = [ ...otherDatas ];
        datas.splice(realData.place - 1, 1);
        datas.splice(userData.place - 1, 0, { name: "Я", value: userData.value });

        if (userData === undefined || datas === undefined) {
            return null;
        }

        let formatedData = {
            labels: [],
            data: [],
            places: [],
            backgroundColor: []
        };

        for (var i = 0; i < datas.length; i++) {
            if (i < valuesCount || i >= datas.length - valuesCount || (i >= userData.place - 2 && i < userData.place + 1) ) {
                isNull = false;

                if (i == userData.place - 1) {
                    formatedData.labels.push('Я');
                    formatedData.places.push(i + 1);
                    formatedData.data.push(userData.value);
                    formatedData.backgroundColor.push('#80c966');
                } else {
                    formatedData.labels.push(datas[i].name);
                    formatedData.places.push(i + 1);
                    formatedData.data.push(datas[i].value);
                    formatedData.backgroundColor.push('#8bc4ea');
                }

            } else if (!isNull) {
                isNull = true;
                formatedData.labels.push('...');
                formatedData.places.push(null);
                formatedData.data.push(0);
                formatedData.backgroundColor.push('rgba(0, 0, 0, 0)');
            }
        }

        return formatedData;
    }

    handleChangePlace(e) {
        if (e.target.value == "") {
            this.setState({
                inputs: {
                    place: ""
                }
            });
            return;
        }
        let newPlace = +e.target.value;
        let { userData, otherDatas } = this.state.data;
        if (newPlace > userData.place) {
            this.setState({
                inputs: {
                    place: newPlace
                }
            });
            return;
        }

        let newVolume = otherDatas[newPlace - 1].value + 1;
        this.setState({
            wishPlace: {
                place: newPlace,
                value: newVolume
            },
            inputs: {
                place: newPlace,
                value: newVolume
            }
        });
    }

    handleChangeVolume(e) {
        let carret = e.target.selectionEnd;
        let lastValue = e.target.value;
        if (e.target.value == "") {
            this.setState({
                inputs: {
                    value: ""
                },
                currentPosition: carret,
                lastValue: lastValue
            });
            return;
        }

        let newVolume = +numeral(e.target.value).value();
        let { userData, otherDatas } = this.state.data;
        if (newVolume < userData.value) {
            this.setState({
                inputs: {
                    value: newVolume
                },
                currentPosition: carret,
                lastValue: lastValue
            });
            return;
        }

        var newPlace;
        for (var i = 0; i < otherDatas.length; i++) {
            if (otherDatas[i].value < newVolume) {
                newPlace = i + 1;
                break;
            }
        }
        this.setState({
            wishPlace: {
                place: newPlace,
                value: newVolume
            },
            inputs: {
                place: newPlace,
                value: newVolume
            },
            currentPosition: carret,
            lastValue: lastValue
        });
    }

    render() {
        const { data, wishPlace, inputs } = this.state;
        self = this;

        const formatData = this.formatData(wishPlace);
        const dataForm = {
            labels: formatData.labels,
            datasets: [{
                label: 'Вложено',
                data: formatData.data.map(function(data){return numeral(data).format('0[.]00')}),
                borderWidth: 1,

                backgroundColor: formatData.backgroundColor,
                datalabels: {
                    anchor: 'end',
                    align: function(context){
                        if (context.chart.data.datasets[context.datasetIndex].data[context.dataIndex] < 2500000) {
                            return 'end';
                        } else {
                            return 'start';
                        }
                    },

                    display: function (context) {
                        return context.chart.data.labels[context.dataIndex] !== '...'
                    }
                }
            }]
        };

        const options = {
            legend: {
                display: false
            },
            maintainAspectRatio: false,
            plugins: {
                datalabels: {
                    formatter: function(value, context) {
                        const { wishPlace } = self.state;
                        const fData = self.formatData(wishPlace);
                        return fData.places[context.dataIndex] + ' место';
                    }
                }
            },
            tooltips: {
                bodyFontSize: 14,
                mode: 'label',
                callbacks: {
                    label: function (tooltipItem, data) {
                        var totalSum = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                        totalSum = parseFloat(totalSum);
                        return "Вложено: " + totalSum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " руб.";
                    }
                }
            },
            scales: {
                xAxes: [{
                    ticks: {
                        // Include a dollar sign in the ticks
                        callback: function(value, index, values) {
                            var totalSum = parseFloat(value);
                            return totalSum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");
                        }
                    }
                }]
            }
        }

        return(
            <div className="inner-row">
                <div className="box col-xl-8 col-lg-7 col-md-12">
                    <div className="chart-container">
                        <Bar data={dataForm} options={options}/>
                    </div>
                </div>
                <div className="circle col-xl-4 col-lg-5 col-md-12">
                    <div className="myPlace">
                        <label>Текущее место:</label><span className="badge badge-pill badge-primary">{data.userData.place}</span><br/>
                        <label>Текущий объем:</label><span className="badge badge-pill badge-primary"> {numeral(data.userData.value).format('0,0[.]00 $')}</span>
                    </div>
                    <div className="wishPanel">
                        <h3>Хочу:</h3>
                        <form>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Место:</label>
                                <input className="col-sm-9 col-form-label" type="text" value={inputs.place} onChange={this.handleChangePlace} />
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Вложения:</label>
                                <input ref='inputValue' className="col-sm-9 col-form-label" type="text" value={numeral(inputs.value).format('0,0.00')} onChange={this.handleChangeVolume} />
                            </div>
                            <div>
                                <label>Дополнительные вложения: {numeral(wishPlace.value - data.userData.value).format('0,0[.]00 $')}</label>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        );
    }
}

ReactDOM.render(<ChartVertBar/>, document.getElementById('row_position'));