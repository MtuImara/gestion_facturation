package backend.rest_api.gestion_facturation.constantes;

public class Constante {

    public enum typeLangue {
        FRENCH {
            @Override
            public String toString() {
                return "French";
            }
        },
        ENGLISH {
            @Override
            public String toString() {
                return "English";
            }
        }
    }

    public enum typeStatut {

        ANNULER {
            @Override
            public String toString() {
                return "annulé(e)";
            }
        },
        EFFECTUER {
            @Override
            public String toString() {
                return "Effectué(e)";
            }
        },
        EN_ATTENTE {
            @Override
            public String toString() {
                return "En Attente";
            }
        },
        ACTIF {
            @Override
            public String toString() {
                return "Actif(ve)";
            }
        },
        DESACTIVER {
            @Override
            public String toString() {
                return "Desactivé(e)";
            }
        },
        ACCEPTER {
            @Override
            public String toString() {
                return "Accepté(e)";
            }
        }

    }
}
