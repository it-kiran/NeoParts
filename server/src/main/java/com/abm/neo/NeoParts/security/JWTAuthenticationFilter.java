package com.abm.neo.NeoParts.security;

public class JWTAuthenticationFilter {

//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private AuthenticationManager authenticationManager;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }


//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req,
//                                                HttpServletResponse res) throws AuthenticationException {
//        try {
//            CustomerDao creds = new ObjectMapper()
//                    .readValue(req.getInputStream(), CustomerDao.class);
//
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            creds.getEmail(),
//                            creds.getPassword(),
//                            new ArrayList<>())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

