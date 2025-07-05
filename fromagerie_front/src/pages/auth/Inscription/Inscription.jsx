import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaFacebook, FaGithub, FaEnvelope, FaUser, FaLock } from "react-icons/fa";
import "@fontsource/poppins/400.css";
import "@fontsource/poppins/600.css";
import "./../../../assets/styles/auth/Inscription.css";

export const Inscription = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSignupClick = () => {
        console.log("Inscription submitted:", { username, email, password });
        navigate("/TableauDeBord"); 
    };

    return (
        <div className="login-container">
            <div className="login-background">
                <div className="welcome-section">
                    <h1 className="welcome-title">Create your Cheesemaker Account</h1>
                    <button className="skip-button">Quick Access</button>
                </div>

                <div className="circle-decor circle-1"></div>
                <div className="login-form-container">
                    <div className="login-form">
                        <div className="form-header">
                            <h2>Sign Up!!!</h2>
                            <p>Join us today!</p>
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
                                    placeholder="Ex: jeandupont"
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <div className="input-with-icon">
                                <FaEnvelope className="input-icon" />
                                <input
                                    id="email"
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder="Ex: jeandupont@gmail.com"
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

                        <button className="login-button" onClick={handleSignupClick}>
                            Sign Up
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
                            <p>Already have an account? <Link to="/login">login</Link></p>
                        </div>
                        <div className="footer-links">
                            <a href="#">Terms & Conditions</a>
                            <a href="#">Support</a>
                            <a href="#">Customer Care</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};