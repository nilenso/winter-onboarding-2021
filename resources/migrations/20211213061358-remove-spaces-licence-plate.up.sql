UPDATE
  cabs
SET
  licence_plate = regexp_replace(licence_plate, '[^a-zA-Z\d:]', '', 'g');
