import {Usv} from "../types/usv.ts";
import UsvCard from "./UsvCard.tsx";
import {useNavigate} from "react-router-dom";

type UsvListProps = {
    usvs: Usv[],
    monitoring: boolean
}

export default function UsvList(props:Readonly<UsvListProps>){
    const navigate = useNavigate()
    return(
        <>
            {props.usvs.map((usv) =>
                <UsvCard usv={usv} monitoring={props.monitoring} key={usv.id}
                                            onClick={() => navigate("/usvdetails/" + usv.id)}/>)}

            {props.monitoring ||        // add a card for adding a new UPS only when not monitoring
                <UsvCard usv={{id:"new", name:"", address:"", community:""}} monitoring={false}
                                            onClick={() => navigate("/newusv")}/>}

        </>
    )
}