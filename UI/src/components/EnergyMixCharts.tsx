import React, {Suspense, useEffect, useState} from 'react';
import { Chart } from 'react-google-charts';
import { avgEnergyMix, Entry } from '../api';

const EnergyMixCharts: React.FC = () => {
    const [data, setData] = useState<Entry[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await avgEnergyMix();
                setData(result);
            } catch (error) {
                alert("Error fetching energy mix data!");
            }
        };
        fetchData();
    }, []);

    if (data.length === 0) {
        return <div className='Loading'>Loading...</div>;
    }

    const colors = [
        '#00b0be',
        '#ffcd8e',
        '#ff8ca1',
        '#98c127',
        '#ffcd8e',
        '#ffb255',
        '#8fd7d7',
        '#bdd373',
        '#f45f74',
    ];

    return (
        <div className='container'>
            {data.map((entry, index) => {
                const chartData = [
                    ['Component', 'Value'],
                    ['biomass', entry.data.biomass],
                    ['other', entry.data.other],
                    ['hydro', entry.data.hydro],
                    ['imports', entry.data.imports],
                    ['gas', entry.data.gas],
                    ['coal', entry.data.coal],
                    ['solar', entry.data.solar],
                    ['nuclear', entry.data.nuclear],
                    ['wind', entry.data.wind]
                ];

                const options = {
                    title: `${index === 0 ? "Today" : index === 1 ? "Tomorrow" : "The day after Tomorrow"} `,
                    titleFontSize: 15,
                    legend: { position: 'bottom' },
                    colors: colors,
                    pieHole: 0.4,
                };

                return (
                    <div className='item' key={index}>
                        <div className='chart'>
                            <Chart
                                chartType="PieChart"
                                width="100%"
                                height="400px"
                                data={chartData}
                                options={options}
                            />
                        </div>
                        <div className='label-cont'>
                            <h4>Clean energy percent:</h4>
                            <label className="label">
                                {entry["clean energy percent"].toFixed(2)}%
                            </label>
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default EnergyMixCharts;
