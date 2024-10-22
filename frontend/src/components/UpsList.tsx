import {Ups} from "../types/ups.ts";
import UpsCard from "./UpsCard.tsx";

type UpsListProps = {
    upss: Ups[],
    monitoring: boolean
}

export default function UpsList(props:Readonly<UpsListProps>){
    return(
        <>
            {props.upss.map((ups) => <UpsCard ups={ups} monitoring={props.monitoring} key={ups.id}/>)}

            {props.monitoring  // add a card for adding a new UPS only when not monitoring
                || <UpsCard ups={{id:"new", name:"", community:"", address:""}} monitoring={false}/>}

        </>
    )
}