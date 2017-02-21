(ns gilded-rose.core)

(def ^:const backstage-pass-name "Backstage passes to a TAFKAL80ETC concert")
(def ^:const elixir-of-mongoose "Elixir of the Mongoose")
(def ^:const dexterity-vest "+5 Dexterity Vest")
(def ^:const sulfuras "Sulfuras, Hand of Ragnaros")
(def ^:const aged-brie "Aged Brie")

(defn- update-quality-for-conjured-item
  [item]
  (let [decrease-in-quality (if (< (:sell-in item) 0) 4 2)]
    (update-in item [:quality] #(max 0 (- % decrease-in-quality)))))

(defn- conjured?
  [item]
  (= (:name item) "Conjured item"))

(defn update-quality [items]
  (map
   (fn [item] (cond
                (conjured? item)
                (update-quality-for-conjured-item item)
                (and (< (:sell-in item) 0) (= backstage-pass-name (:name item)))
                (merge item {:quality 0})
                (or (= (:name item) aged-brie) (= (:name item) backstage-pass-name))
                (if (and (= (:name item) backstage-pass-name) (>= (:sell-in item) 5) (< (:sell-in item) 10))
                  (merge item {:quality (inc (inc (:quality item)))})
                  (if (and (= (:name item) backstage-pass-name) (>= (:sell-in item) 0) (< (:sell-in item) 5))
                    (merge item {:quality (inc (inc (inc (:quality item))))})
                    (if (< (:quality item) 50)
                      (merge item {:quality (inc (:quality item))})
                      item)))
                (< (:sell-in item) 0)
                (if (= backstage-pass-name (:name item))
                  (merge item {:quality 0})
                  (if (or (= dexterity-vest (:name item)) (= elixir-of-mongoose (:name item)))
                    (merge item {:quality (- (:quality item) 2)})
                    item))
                (or (= dexterity-vest (:name item)) (= elixir-of-mongoose (:name item)))
                (merge item {:quality (dec (:quality item))})
                :else item))
   (map (fn [item]
          (if (not= sulfuras (:name item))
            (merge item {:sell-in (dec (:sell-in item))})
            item))
        items)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory []
  (let [inventory
        [(item dexterity-vest 10 20)
         (item aged-brie 2 0)
         (item elixir-of-mongoose 5 7)
         (item sulfuras 0 80)
         (item backstage-pass-name 15 20)]]
    (update-quality inventory)))
