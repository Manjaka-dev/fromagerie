import React, { useState } from "react";
import { useNavigate , Link  } from "react-router-dom";
import { FaFacebook, FaGithub, FaEnvelope, FaUser, FaLock } from "react-icons/fa";
import "@fontsource/poppins/400.css";  // Regular
import "@fontsource/poppins/600.css";  // Semi-Bold (pour les titres)
import "./../../../assets/styles/auth/login.css";

export const Login = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLoginClick = () => {
    console.log("Username:", username);
    console.log("Password:", password);
    navigate("/TableauDeBord");
  };

  return (
    <div className="login-container">
      <div className="login-background">
        <div className="welcome-section">
          <h1 className="welcome-title">Welcome to the Cheesemaker's Portal</h1>
          <button className="skip-button">Quick Access</button>

        </div>


        <div className="circle-decor ellipse"></div>
        <div className="login-form-container">
          <div className="login-form">
            <div className="form-header">
              <h2>Login</h2>
              <p>Glad you're back!</p>
            </div>

            <div className="form-group">
              <label htmlFor="username">Username</label>
              <div className="input-with-icon">
                <FaUser className="input-icon" />
                <input
                  id="username"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Ex : jeandupont"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <div className="input-with-icon">
                <FaLock className="input-icon" />
                <input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="********"
                />
              </div>
            </div>

            <div className="remember-forgot">
              <div className="remember-me">
                <input type="checkbox" id="remember" />
                <label htmlFor="remember">Remember me</label>
              </div>
              <a href="#" className="forgot-password">Forgot password?</a>
            </div>

            <button className="login-button" onClick={handleLoginClick}>
              Login
            </button>

            <div className="divider">
              <span className="divider-line"></span>
              <span className="divider-text">Or</span>
              <span className="divider-line"></span>
            </div>

            <div className="social-logins">
              <a href="#" className="social-icon facebook" onClick={() => handleSocialLogin('Facebook')}>
                <FaFacebook />
              </a>
              <a href="#" className="social-icon github" onClick={() => handleSocialLogin('GitHub')}>
                <FaGithub />
              </a>
              <a href="#" className="social-icon email" onClick={() => handleSocialLogin('Email')}>
                <FaEnvelope />
              </a>
            </div>

            <div className="signup-link">
              <p className="signup">Don't have an account? <Link to="/inscription">Signup</Link></p>
            </div>

            <div className="footer-links">
              <a href="#">Terms & Conditions</a>
              <a href="#">Support</a>
              <a href="#">Customer Care</a>
            </div>
          </div>
        </div>
      </div>
      <div className="circle-decor ellipse-2"></div>
    </div>
  );
};