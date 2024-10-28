import {Credentials} from "./credentials.ts";

export type Server = {
    id: string,
    name: string,
    address: string,
    credentials: Credentials,
    upsId: string,
    shutdownTime: number
}