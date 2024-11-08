import {Status} from "../types/status.ts";

type Props = {
    status: Status|undefined
}

export default function StatusInfo (props: Readonly<Props>){
    return(
        <div className={props.status?.state==="POWER_OFF" ?  "status-poweroff" : "status-ok"}>
            <p>{props.status?.state} since {props.status?.since}</p>
            <p>{props.status?.remaining} seconds battery time</p>
        </div>
    )
}