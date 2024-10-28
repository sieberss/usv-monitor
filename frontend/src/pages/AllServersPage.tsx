import UpsList from "../components/UpsList.tsx";
import {Ups} from "../types/ups.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {Server} from "../types/server.ts";

type OverviewProps = {
    upses: Ups[],
    monitoring: boolean
}

export default function AllUpsesPage(props:Readonly<OverviewProps>){
     return(
        <>
            {props.monitoring ? <h3> Monitoring mode </h3> : <h3> no Monitoring</h3>}
            <h1>List of Servers</h1>
            <ServerList servers={servers} upses={props.upses} monitoring={props.monitoring}/>
        </>
    )
}
