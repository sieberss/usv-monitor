import UpsList from "../components/UpsList.tsx";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";

type AllUpsesProps = {
    upses: Ups[],
    servers: Server[],
    monitoring: boolean,
    getUpsClassName: (id: string) => string
}

export default function AllUpsesPage(props:Readonly<AllUpsesProps>){
    return(
        <UpsList upses={props.upses} servers={props.servers} monitoring={props.monitoring} getUpsClassName={props.getUpsClassName}/>
    )
}
