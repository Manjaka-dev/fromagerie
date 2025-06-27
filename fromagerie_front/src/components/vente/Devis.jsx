import React from "react";
import { CalendarOutline } from "./CalendarOutline";
import add from "./add.svg";
import "./style.css";
import vector from "./vector.svg";

export const Devis = () => {
  return (
    <div className="devis">
      <div className="frame">
        <div className="text-wrapper">Livré</div>

        <div className="overlap-group">
          <div className="div">2</div>
        </div>

        <img className="vector" alt="Vector" src={vector} />
      </div>

      <div className="frame-2">
        <div className="text-wrapper-2">Fromagerie Martin</div>

        <div className="frame-wrapper">
          <div className="div-wrapper">
            <div className="text-wrapper-3">Haute</div>
          </div>
        </div>

        <div className="frame-3">
          <div className="text-wrapper-4">200 000 Ar</div>

          <div className="text-wrapper-5">Contact : M.Martin</div>

          <p className="p">Gouda affiné : 50 kg</p>

          <CalendarOutline className="calendar-outline" />
          <div className="text-wrapper-6">2025-06-05</div>
        </div>
      </div>

      <div className="frame-4">
        <div className="text-wrapper-2">Fromagerie Martin</div>

        <div className="frame-5">
          <div className="frame-6">
            <div className="text-wrapper-7">Moyenne</div>
          </div>
        </div>

        <div className="frame-3">
          <div className="text-wrapper-4">200 000 Ar</div>

          <div className="text-wrapper-5">Contact : M.Martin</div>

          <p className="p">Gouda affiné : 50 kg</p>

          <CalendarOutline className="calendar-outline" />
          <div className="text-wrapper-6">2025-06-05</div>
        </div>
      </div>

      <div className="frame-7">
        <img className="add" alt="Add" src={add} />

        <div className="text-wrapper-8">Ajouter</div>
      </div>
    </div>
  );
};