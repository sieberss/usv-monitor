import UpsList from "../components/UpsList.tsx";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";

type AllUpsesProps = {
    setMenuItem: (item:string) => void,
    upses: Ups[],
    servers: Server[]
    monitoring: boolean,
    username: string
}

export default function AllUpsesPage(props:Readonly<AllUpsesProps>){
    props.setMenuItem("ups")
    return(
        <div className={"ups"}>
            {props.monitoring ? <h3> Monitoring mode </h3> : <h3> no Monitoring</h3>}
            <h1>UPSes</h1>
            <UpsList upses={props.upses} servers={props.servers} monitoring={props.monitoring}/>
        </div>
    )
}
