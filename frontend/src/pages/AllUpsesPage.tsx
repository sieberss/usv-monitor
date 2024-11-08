import UpsList from "../components/UpsList.tsx";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import {Status} from "../types/status.ts";

type AllUpsesProps = {
    upses: Ups[],
    servers: Server[],
    monitoring: boolean,
    getUpsStatus: (id: string) => Status | undefined
}

export default function AllUpsesPage(props:Readonly<AllUpsesProps>){
    return(
        <UpsList upses={props.upses} servers={props.servers} monitoring={props.monitoring} getUpsStatus={props.getUpsStatus}/>
    )
}
