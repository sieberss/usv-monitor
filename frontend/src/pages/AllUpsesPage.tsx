import UpsList from "../components/UpsList.tsx";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";

type AllUpsesProps = {
    setMenuItem: (item:string) => void,
    upses: Ups[],
    servers: Server[]
    monitoring: boolean
}

export default function AllUpsesPage(props:Readonly<AllUpsesProps>){
    props.setMenuItem("ups")
    return(
        <UpsList upses={props.upses} servers={props.servers} monitoring={props.monitoring}/>
    )
}
