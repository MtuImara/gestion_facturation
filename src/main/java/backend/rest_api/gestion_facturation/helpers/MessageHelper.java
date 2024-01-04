package backend.rest_api.gestion_facturation.helpers;

public class MessageHelper {
    public static String success() {
        return "Operation effectuee avec success";
    }

    public static String message(String msg) {
        return msg;
    }

    public static String error(String message) {
        return message;
    }

    public static String unauthorized() {
        return "Authentification requise";
    }

    public static String forbidden() {
        return "Autorisation requise";
    }

    public static String badToken() {
        return "Jeton d'authentification invalide";
    }

    public static String loginFail() {
        return "Parametres d'authentification incorrects";
    }

    public static String loginFail(String note) {
        return MessageHelper.loginFail() + ": " + note;
    }

    public static String loginSuccess() {
        return "Authentification effectuee avec success!";
    }

    public static String blocked() {
        return "L' utilisateur a ete bloque";
    }

    public static String enabledUser() {
        return "L'utilisateur a ete desactive";
    }

    public static String internalServer() {
        return "Quelque chose a mal tournee";
    }

    public static String methodNotAllowed() {
        return "Methode non autorisee";
    }

    public static String notAcceptable() {
        return "requete non acceptable";
    }

    public static String timeOut() {
        return "Le temps de la requete depasse";
    }

    public static String conflict() {
        return "Un conflict a ete signale lors de la requete";
    }

    public static String internalServer(String exception_message) {
        return "Quelque chose a mal tournée ou cette donnée est utilisée dans une ou d'autres gestion(s): "
                + exception_message;
    }

    public static String unprocessed() {
        return "Une erreur semantique a ete constatee";
    }

    public static String createdSuccessfully() {
        return "Enregistrement effectue avec success!";
    }

    public static String createFail() {
        return "Echec d'enregistrement";
    }

    public static String deleteFail() {

        return "Echec de suppression les donnes sont deja utilise";
    }

    public static String createFail(String name) {
        return "Echec d'enregistrement : " + name;
    }

    public static String createdSuccessfully(String name) {
        return name + " enregistre avec success!";
    }

    public static String updatedSuccessFully() {
        return "Mise a jour affectuee avec success!";
    }

    public static String updatedSuccessfully(String name) {
        return "Mise a jour de " + name + " effectue avec success!";
    }

    public static String notFound() {
        return "Ressource introuvable";
    }

    public static String notFound(String name) {
        return name + " introuvable!";
    }

    public static String noContent() {
        return "Aucune donnee a afficher!";
    }

    public static String deletedSuccessFully() {
        return "Enregistrement supprime avec success!";
    }

    public static String noContent(String name) {
        return "Aucun " + name + " a afficher";
    }

    public static String dataExist(String name) {
        return name + "  est deja utiliser ";
    }

    public static String canNotBeNull() {
        return "Ne peut etre null";
    }

    public static String canNotBeNull(String name) {

        return name + " " + canNotBeNull();
    }

    public static String unabletodelete() {
        return "Echec de suppression, Element est lié à d'autres données";
    }

    public static String Object(String donnes) {
        return donnes;
    }

    public static String codeBankNombre() {
        return "Le Code de la Banque doit avoir cinq (5) chiffres ";
    }

    public static String codeDeviseTroisCaractere() {
        return "Le Code de la Devise doit avoir trois (3) caractères ";
    }

    public static String codeBankPasCaractere() {
        return "Le Code de la Banque doit pas contenir des caratères ";
    }

    public static String dureeMinNoInferieurDureeMax() {
        return "La durée Minimale ne doit pas être supérieur à la durée Maximale";
    }

    public static String deviseToujourEncours() {
        return "Cette devise est toujour valide, veillez cloturer sa date de validité avant de la saisir";
    }

    public static String dateIncluseDansUneDejaCloturer() {
        return "Cette date est incluse dans une date déjà cloturée, veuillez saisir une autre date";
    }

    public static String reportsAnouveaux() {
        return "Les (reports) A Nouveaux de l'exercice en cours ont déjà été génerés";
    }

    public static String quantiteInsuffisante() {
        return "La Quantité en stock est inférieure à celle demandée";
    }
}
