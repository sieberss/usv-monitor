import {Usv} from "../types/usv.ts";
import UsvCard from "./UsvCard.tsx";

type UsvListProps = {
    usvs: Usv[],
    monitoring: boolean
}

export default function UsvList(props:Readonly<UsvListProps>){
    return(
        <>
            {props.usvs.map((usv) => <UsvCard usv={usv} monitoring={props.monitoring} key={usv.id}/>)}

            {props.monitoring  // add a card for adding a new UPS only when not monitoring
                || <UsvCard usv={{id:"new", name:"", community:"", address:""}} monitoring={false}/>}

        </>
    )
}