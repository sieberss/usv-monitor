import {Usv} from "../types/usv.ts";
import {FaPlus} from "react-icons/fa";
import './UsvCard.css'

type UsvCardProps = {
    usv: Usv,
    monitoring: boolean
    onClick: () => void
}

export default function UsvCard(props: Readonly<UsvCardProps>) {
    return (
        <>
            {props.usv.id !== "new"
                ? <div >
                    <h3>{props.usv.name}</h3>
                    <p>{props.usv.address}</p>
                  </div>
                : // plus-button for adding
                  <div>
                      <h2><FaPlus/></h2>
                  </div>
            }
        </>
    )
}