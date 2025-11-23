export interface DataFields {
    biomass: number;
    other: number;
    hydro: number;
    imports: number;
    gas: number;
    coal: number;
    solar: number;
    nuclear: number;
    wind: number;
}

export interface Entry {
    data: DataFields;
    from: string;
    to: string;
    "clean energy percent": number;
}

export interface Data {
    from: string;
    to: string;
    "clean energy percent": number;
}

export async function avgEnergyMix(): Promise<Entry[]> {
    const url = '/api/v1/avgEnergyMix';
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), 10000);
    try {
        const res = await fetch(url, { signal: controller.signal, credentials: 'omit' });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = (await res.json()) as Entry[];
        return data;
    } finally {
        clearTimeout(timer);
    }
}

export async function bestInterval(val: number): Promise<Data> {
    const url = '/api/v1/bestInterval';
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), 10000);
    try {
        const res = await fetch(url + "?duration=" + val, { signal: controller.signal, credentials: 'omit' });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = (await res.json()) as Data;
        return data;
    } finally {
        clearTimeout(timer);
    }
}