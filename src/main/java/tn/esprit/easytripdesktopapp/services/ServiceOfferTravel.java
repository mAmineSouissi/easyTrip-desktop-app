package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.models.Category;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ServiceOfferTravel implements CRUDService<OfferTravel> {

    private Connection cnx ;

    public ServiceOfferTravel(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(OfferTravel offer) {
        String qry = "INSERT INTO `offer_travel`(`departure`, `destination`, `departure_date`, `arrival_date`, `hotelName`, `flightName`, `discription`, `price`,  `image`, `agency_id`,`promotion_id`, `category`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, offer.getDeparture());
            pstm.setString(2, offer.getDestination());


            if (offer.getDeparture_date() != null) {
                pstm.setDate(3, new java.sql.Date(offer.getDeparture_date().getTime()));
            } else {
                pstm.setNull(3, java.sql.Types.DATE);
            }

            if (offer.getArrival_date() != null) {
                pstm.setDate(4, new java.sql.Date(offer.getArrival_date().getTime()));
            } else {
                pstm.setNull(4, java.sql.Types.DATE);
            }

            pstm.setString(5, offer.getHotelName());
            pstm.setString(6, offer.getFlightName());
            pstm.setString(7, offer.getDiscription());
            pstm.setFloat(8, offer.getPrice());
            pstm.setString(9, offer.getImage());
            pstm.setInt(10,offer.getAgence().getId());
            if (offer.getPromotion()!=null)
                pstm.setInt(11, offer.getPromotion().getId());
            else
                pstm.setNull(11, java.sql.Types.INTEGER);

            pstm.setString(12, offer.getCategory().name());

            pstm.executeUpdate();
            System.out.println("Offre ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<OfferTravel> getAll() {
        List<OfferTravel> offers = new ArrayList<>();
        String qry = "SELECT * FROM `offer_travel`";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture_date(rs.getDate("departure_date"));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotelName"));
                offer.setFlightName(rs.getString("flightName"));
                offer.setDiscription(rs.getString("discription"));
                offer.setPrice(rs.getFloat("price"));
                offer.setImage(rs.getString("image"));

                int agenceId = rs.getInt("agency_id");
                int promotionId = rs.getInt("promotion_id");

                // Récupérer l'Agence en utilisant ServiceAgence
                ServiceAgence serviceAgence = new ServiceAgence();
                Optional<Agence> agenceOptional = serviceAgence.getByid(agenceId);
                Agence agence = agenceOptional.orElse(null);
                offer.setAgence(agence);

                // Récupérer la Promotion en utilisant ServicePromotion
                ServicePromotion servicePromotion = new ServicePromotion();
                Optional<Promotion> promotionOptional = servicePromotion.getByid(promotionId);
                Promotion promotion = promotionOptional.orElse(null);
                offer.setPromotion(promotion);

                //Récupérer category
                String categoryString = rs.getString("category");
                Category category = null;
                if(categoryString != null){
                    category = Category.valueOf(categoryString);
                }

                offer.setCategory(category);

                offers.add(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offers;
    }


    @Override
    public void update(OfferTravel offer) {
        String qry = "UPDATE `offer_travel` SET `departure`=?, `destination`=?, `departure_date`=?, `arrival_date`=?, `hotelName`=?, `flightName`=?, `discription`=?, `price`=?, `image`=?, `agency_id`=?, `promotion_id`=?, `category`=? WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, offer.getDeparture());
            pstm.setString(2, offer.getDestination());

            // Handle possible null Date values
            if (offer.getDeparture_date() != null) {
                pstm.setDate(3, new java.sql.Date(offer.getDeparture_date().getTime()));
            } else {
                pstm.setNull(3, java.sql.Types.DATE);
            }

            if (offer.getArrival_date() != null) {
                pstm.setDate(4, new java.sql.Date(offer.getArrival_date().getTime()));
            } else {
                pstm.setNull(4, java.sql.Types.DATE);
            }

            pstm.setString(5, offer.getHotelName());
            pstm.setString(6, offer.getFlightName());
            pstm.setString(7, offer.getDiscription());
            pstm.setFloat(8, offer.getPrice());
            pstm.setString(9, offer.getImage());
            pstm.setInt(10, offer.getAgence().getId());
            if (offer.getPromotion() != null) {
                pstm.setInt(11, offer.getPromotion().getId());
            } else {
                pstm.setNull(11, java.sql.Types.INTEGER);
            }
            pstm.setString(12, offer.getCategory().name());
            pstm.setInt(13, offer.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Offre mise à jour avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(OfferTravel offer) {
        String qry = "DELETE FROM `offer_travel` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, offer.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Offre supprimée avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public OfferTravel getById(int id) {
        String qry = "SELECT * FROM `offer_travel` WHERE `id`=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture(String.valueOf(rs.getDate("departure_date")));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotel_name"));
                offer.setFlightName(rs.getString("flight_name"));
                offer.setDiscription(rs.getString("description"));
                offer.setPrice(rs.getFloat("price"));
                offer.setImage(rs.getString("image"));
                int agencyId = rs.getInt("agency_id");
                int promotionId = rs.getInt("promotion_id");

                // Fetch agency using ServiceAgence
                ServiceAgence serviceAgence = new ServiceAgence();
                Agence agence = serviceAgence.getById(agencyId);
                offer.setAgence(agence);

                // Fetch promotion using ServicePromotion
                ServicePromotion servicePromotion = new ServicePromotion();
                Optional<Promotion> promotionOptional = servicePromotion.getByid(promotionId);
                Promotion promotion = promotionOptional.orElse(null);
                offer.setPromotion(promotion);

                // Fetch category
                String categoryString = rs.getString("category");
                Category category = null;
                if (categoryString != null) {
                    try {
                        category = Category.valueOf(categoryString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid category value: " + categoryString);
                    }
                }
                offer.setCategory(category);

                return offer;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching offer by ID: " + e.getMessage());
        }
        return null; // Return null if no offer is found
    }

    public Optional<OfferTravel> getByid(int id) {
        String qry = "SELECT * FROM `offer_travel` WHERE `id`=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture_date(rs.getDate("departure_date"));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotelName"));
                offer.setFlightName(rs.getString("flightName"));
                offer.setDiscription(rs.getString("discription"));
                offer.setPrice(rs.getFloat("price"));
                offer.setImage(rs.getString("image"));

                int agenceId = rs.getInt("agency_id");
                int promotionId = rs.getInt("promotion_id");

                // Récupérer l'Agence en utilisant ServiceAgence
                ServiceAgence serviceAgence = new ServiceAgence();
                Agence agenceOptional = serviceAgence.getById(agenceId);
                offer.setAgence(agenceOptional);

                // Récupérer la Promotion en utilisant ServicePromotion
                ServicePromotion servicePromotion = new ServicePromotion();
                Optional<Promotion> promotionOptional = servicePromotion.getByid(promotionId);
                Promotion promotion = promotionOptional.orElse(null);
                offer.setPromotion(promotion);

                //Récupérer category
                String categoryString = rs.getString("category");
                Category category = null;
                if(categoryString != null){
                    category = Category.valueOf(categoryString);
                }

                offer.setCategory(category);

                return Optional.of(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<OfferTravel> search(String keyword) {
        List<OfferTravel> offers = new ArrayList<>();
        String qry = "SELECT * FROM `offer_travel` WHERE `departure` LIKE ? OR `destination` LIKE ? OR `hotelName` LIKE ? OR `description` LIKE ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            String likeKeyword = "%" + keyword + "%";
            pstm.setString(1, likeKeyword);
            pstm.setString(2, likeKeyword);
            pstm.setString(3, likeKeyword);
            pstm.setString(4, likeKeyword);

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture_date(rs.getDate("departure_date"));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotelName"));
                offer.setFlightName(rs.getString("flightName"));
                offer.setDiscription(rs.getString("discription"));
                offer.setPrice(rs.getFloat("price"));
                offer.setImage(rs.getString("image"));

                int agenceId = rs.getInt("agency_id");
                int promotionId = rs.getInt("promotion_id");

                // Récupérer l'Agence en utilisant ServiceAgence
                ServiceAgence serviceAgence = new ServiceAgence();
                Agence agenceOptional = serviceAgence.getById(agenceId);
                offer.setAgence(agenceOptional);

                // Récupérer la Promotion en utilisant ServicePromotion
                ServicePromotion servicePromotion = new ServicePromotion();
                Optional<Promotion> promotionOptional = servicePromotion.getByid(promotionId);
                Promotion promotion = promotionOptional.orElse(null);
                offer.setPromotion(promotion);

                //Récupérer category
                String categoryString = rs.getString("category");
                Category category = null;
                if(categoryString != null){
                    category = Category.valueOf(categoryString);
                }

                offer.setCategory(category);


                offers.add(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offers;
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT COUNT(*) FROM `offer_travel` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Override
    public long count() {
        String qry = "SELECT COUNT(*) FROM `offer_travel`";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }


    public List<OfferTravel> getOffresByAgenceId(int agenceId) {
        List<OfferTravel> offers = new ArrayList<>();
        String qry = "SELECT offer_travel.*, " +
                "       promotion.id AS id, promotion.title AS promotion_title, promotion.description AS promotion_description, promotion.discount_percentage AS promotion_discount_percentage, promotion.valid_until AS promotion_valid_until " +
                "FROM `offer_travel` " +
                "LEFT JOIN `promotion` ON offer_travel.promotion_id = promotion.id "+
                " WHERE `agency_id`=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, agenceId);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture_date(rs.getDate("departure_date"));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotelName"));
                offer.setFlightName(rs.getString("flightName"));
                offer.setDiscription(rs.getString("discription"));
                offer.setPrice(rs.getFloat("price"));
                offer.setImage(rs.getString("image"));


                String categoryString = rs.getString("category");
                Category category = null;
                if(categoryString != null){
                    category = Category.valueOf(categoryString);
                }

                offer.setCategory(category);

                Promotion promotion = null;
                int promotionId = rs.getInt("promotion_id");
                if (!rs.wasNull()) {
                    promotion = new Promotion();
                    promotion.setId(promotionId);
                    promotion.setTitle(rs.getString("title"));
                    promotion.setDescription(rs.getString("description"));
                    promotion.setDiscount_percentage(rs.getFloat("discount_percentage"));
                    promotion.setValid_until(rs.getDate("valid_until"));
                    offer.setPromotion(promotion);
                }

                offers.add(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offers;
    }

    public List<OfferTravel> getOffresByPromotionId(int promotionId) {
        List<OfferTravel> offers = new ArrayList<>();
        String qry = "SELECT offer_travel.*, " +
                "       agency.id AS agence_id, agency.name AS agence_nom, agency.address AS agence_address, agency.phone AS agence_phone, agency.email AS agence_email, agency.image AS agence_image " +
                "FROM `offer_travel` " +
                "JOIN `agency` ON offer_travel.agency_id = agency.id " +
                " WHERE `promotion_id`=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, promotionId);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture_date(rs.getDate("departure_date"));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotelName"));
                offer.setFlightName(rs.getString("flightName"));
                offer.setDiscription(rs.getString("description"));
                offer.setPrice(rs.getFloat("price"));
                offer.setImage(rs.getString("image"));


                String categoryString = rs.getString("category");
                Category category = null;
                if(categoryString != null){
                    category = Category.valueOf(categoryString);
                }
                offer.setCategory(category);

                Agence agence = new Agence();
                agence.setId(rs.getInt("id"));
                agence.setNom(rs.getString("name"));
                agence.setAddress(rs.getString("address"));
                agence.setPhone(rs.getString("phone"));
                agence.setEmail(rs.getString("email"));
                agence.setImage(rs.getString("image"));

                offer.setAgence(agence);
                offers.add(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offers;
    }


    public Map<Agence, Integer> getOfferCountByAgency() {
        Map<Agence, Integer> offerCountByAgency = new HashMap<>();
        String query = "SELECT a.id, a.name, COUNT(ot.id) as offer_count " +
                "FROM agency a " +
                "LEFT JOIN offer_travel ot ON a.id = ot.agency_id " +
                "GROUP BY a.id, a.name";
        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Agence agence = new Agence();
                agence.setId(rs.getInt("id"));
                agence.setNom(rs.getString("name"));
                int offerCount = rs.getInt("offer_count");
                offerCountByAgency.put(agence, offerCount);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des offres par agence : " + e.getMessage());
        }
        return offerCountByAgency;
    }
}